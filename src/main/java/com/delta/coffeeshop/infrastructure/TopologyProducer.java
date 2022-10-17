package com.delta.coffeeshop.infrastructure;

import com.delta.coffeeshop.counter.domain.Order;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.dao.DynamoDBDao;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderEventResult;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.stream.Collectors;

@ApplicationScoped
public class TopologyProducer {

    final Logger logger = LoggerFactory.getLogger(TopologyProducer.class);

    @ConfigProperty(name = "topic.orders-in")
    protected String ORDERS_IN;

    @ConfigProperty(name = "topic.kitchen-in")
    protected String KITCHEN_IN;

    @ConfigProperty(name = "topic.barista-in")
    protected String BARISTA_IN;

    @ConfigProperty(name = "topic.web-updates")
    protected String WEB_UPDATES;

    @Inject
    DynamoDBDao orderRepository;

    @Produces
    public Topology buildTopology() {
        var om = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        var placeOrderCommandSerde = new ObjectMapperSerde<>(PlaceOrderCommand.class, om);
        var orderTicketSerde = new ObjectMapperSerde<>(OrderTicket.class, om);
        var orderUpdateSerde = new ObjectMapperSerde<>(OrderUpdate.class, om);

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, PlaceOrderCommand> commandsStream = builder.stream(ORDERS_IN,
                Consumed.with(Serdes.String(), placeOrderCommandSerde));
        KStream<String, OrderEventResult> orderStream = commandsStream
                .map((key, command) -> new KeyValue<>(command.getId(), Order.process(command)))
                .peek((key, result) -> orderRepository.persist(result.getOrder()))
                .peek((key, result) -> logger.debug("PERSISTED RESULT: " + result));

        orderStream
                .filter((id, order) -> order.getBaristaTickets().isPresent())
                .peek((key, order) -> logger.debug("BARISTA: key: " + key + ", order: " + order))
                .flatMap((key, order) -> order.getBaristaTickets().get().stream().map(ticket -> new KeyValue<>(key, ticket)).collect(Collectors.toList()))
                .to(BARISTA_IN,
                        Produced.with(Serdes.String(), orderTicketSerde));

        orderStream
                .filter((id, order) -> order.getKitchenTickets().isPresent())
                .peek((key, order) -> logger.debug("KITCHEN: key: " + key + ", order: " + order))
                .flatMap((key, order) -> order.getKitchenTickets().get().stream().map(ticket -> new KeyValue<>(key, ticket)).collect(Collectors.toList()))
                .to(KITCHEN_IN,
                        Produced.with(Serdes.String(), orderTicketSerde));

        orderStream
                .filter((id, order) -> !order.getOrderUpdates().isEmpty())
                .peek((key, order) -> logger.debug("ORDER UPDATES: key: " + key + ", order: " + order))
                .flatMap((key, order) -> order.getOrderUpdates().stream().map(orderUpdate -> new KeyValue<>(key, orderUpdate)).collect(Collectors.toList()))
                .to(WEB_UPDATES,
                        Produced.with(Serdes.String(), orderUpdateSerde));

        return builder.build();
    }
}