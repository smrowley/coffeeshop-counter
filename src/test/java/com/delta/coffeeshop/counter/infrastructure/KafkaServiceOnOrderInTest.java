package com.delta.coffeeshop.counter.infrastructure;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Properties;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.OrderStatus;
import com.delta.coffeeshop.counter.domain.commands.CommandItem;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.dao.DynamoDBDao;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderUpdate;
import com.delta.coffeeshop.infrastructure.KafkaService;
import com.delta.coffeeshop.infrastructure.OrderService;
import com.delta.coffeeshop.testing.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;


@QuarkusTest
@Transactional
public class KafkaServiceOnOrderInTest {

    @ConfigProperty(name = "topic.orders-in")
    protected String ORDERS_IN;

    protected String BARISTA = "barista";

    @ConfigProperty(name = "topic.kitchen-in")
    protected String KITCHEN_IN;

    @ConfigProperty(name = "topic.barista-in")
    protected String BARISTA_IN;

    @ConfigProperty(name = "topic.web-updates")
    protected String WEB_UPDATES;

    @InjectMock
    DynamoDBDao orderRepository;

    private TopologyTestDriver topologyTestDriver;

    @Inject
    private Topology topology;

    private TestInputTopic<String, PlaceOrderCommand> orderInTopic;
    private TestOutputTopic<String, OrderTicket> kitchenInTopic;
    private TestOutputTopic<String, OrderTicket> baristaInTopic;
    private TestOutputTopic<String, OrderUpdate> webUpdatesTopic;

    @BeforeEach
    public void setUp() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        config.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");

        var om = new ObjectMapper();
        om.registerModule(new Jdk8Module());
        om.registerModule(new JavaTimeModule());

        topologyTestDriver = new TopologyTestDriver(topology, config);
        orderInTopic = topologyTestDriver.createInputTopic(ORDERS_IN, Serdes.String().serializer(), new ObjectMapperSerializer<>(om));
        kitchenInTopic = topologyTestDriver.createOutputTopic(KITCHEN_IN, Serdes.String().deserializer(), new ObjectMapperDeserializer<OrderTicket>(OrderTicket.class, om));
        baristaInTopic = topologyTestDriver.createOutputTopic(BARISTA_IN, Serdes.String().deserializer(), new ObjectMapperDeserializer<OrderTicket>(OrderTicket.class, om));
        webUpdatesTopic = topologyTestDriver.createOutputTopic(WEB_UPDATES, Serdes.String().deserializer(), new ObjectMapperDeserializer<OrderUpdate>(OrderUpdate.class, om));
    }

    /**
     * Verify that the appropriate method is called on OrderService when a PlaceOrderCommand is received
     *
     * @see OrderService
     * @see PlaceOrderCommand
     */
    @Test
    public void testOrderIn() {
        PlaceOrderCommand placeOrderCommand = TestUtil.stubPlaceOrderCommand();
        orderInTopic.pipeInput(placeOrderCommand);

        assertThat(kitchenInTopic.getQueueSize(), equalTo(0L));
        assertThat(baristaInTopic.getQueueSize(), equalTo(1L));
        assertThat(webUpdatesTopic.getQueueSize(), equalTo(1L));

        // Expected Web update: OrderUpdate[orderId='guid', itemId='guid', name='Foo', item=COFFEE_BLACK, status=IN_PROGRESS, madeBy='null']
        OrderUpdate orderUpdate = webUpdatesTopic.readValue();
        assertThat(orderUpdate.getOrderId(), equalTo(orderUpdate.getOrderId()));
        CommandItem baristaItem = placeOrderCommand.getBaristaLineItems().get().get(0);
        assertThat(orderUpdate.getItem(), equalTo(baristaItem.getItem()));
        assertThat(orderUpdate.getName(), equalTo(baristaItem.getName()));
        assertThat(orderUpdate.getStatus(), equalTo(OrderStatus.IN_PROGRESS));
 
        // Expected Order Ticket update: OrderTicket{orderId='guid', id=guid, item=COFFEE_BLACK, name='Foo', timestamp=2021-06-11T19:12:25.660556400Z}
        OrderTicket orderTicket = baristaInTopic.readValue();
        assertThat(orderTicket.getOrderId(), equalTo(orderTicket.getOrderId()));
        assertThat(orderTicket.getItem(), equalTo(baristaItem.getItem()));
        assertThat(orderTicket.getName(), equalTo(baristaItem.getName()));
     }

}
