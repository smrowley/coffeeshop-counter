package com.delta.coffeeshop.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.delta.coffeeshop.counter.domain.Order;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.dao.DynamoDBDao;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderEventResult;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderUpdate;
import com.delta.coffeeshop.counter.domain.valueobjects.TicketUp;

@ApplicationScoped
public class OrderService {

    final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    ThreadContext threadContext;

    @Inject
    DynamoDBDao orderRepository;

    @Channel("barista")
    Emitter<OrderTicket> baristaEmitter;

    @Channel("kitchen")
    Emitter<OrderTicket> kitchenEmitter;

    @Channel("web-updates")
    Emitter<OrderUpdate> orderUpdateEmitter;

    public void onOrderIn(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("onOrderIn {}", placeOrderCommand);

        OrderEventResult orderEventResult = Order.process(placeOrderCommand);

        logger.debug("OrderEventResult returned: {}", orderEventResult);

        orderRepository.persist(orderEventResult.getOrder());

        /*
         * orderEventResult.getOutboxEvents().forEach(exportedEvent -> { logger.debug("Firing event: {}", exportedEvent); event.fire(exportedEvent); });
         */

        if (orderEventResult.getBaristaTickets().isPresent()) {
            orderEventResult.getBaristaTickets().get().forEach(baristaTicket -> {
                logger.debug("Sending Ticket to Barista Service: {}", baristaTicket);
                baristaEmitter.send(baristaTicket);
            });
        }

        if (orderEventResult.getKitchenTickets().isPresent()) {
            orderEventResult.getKitchenTickets().get().forEach(kitchenTicket -> {
                kitchenEmitter.send(kitchenTicket);
            });
        }

        orderEventResult.getOrderUpdates().forEach(orderUpdate -> {
            orderUpdateEmitter.send(orderUpdate);
        });

    }

    public void onOrderUp(final TicketUp ticketUp) {

        logger.debug("onOrderUp: {}", ticketUp);
        Order order = orderRepository.findById(ticketUp.getOrderId());
        OrderEventResult orderEventResult = order.applyOrderTicketUp(ticketUp);
        logger.debug("OrderEventResult returned: {}", orderEventResult);
        orderRepository.persist(orderEventResult.getOrder());
        orderEventResult.getOrderUpdates().forEach(orderUpdate -> {
            orderUpdateEmitter.send(orderUpdate);
        });
    }

    @Override
    public String toString() {
        return "OrderService{" + "threadContext=" + threadContext + ", orderRepository="
                + orderRepository + ", baristaEmitter=" + baristaEmitter + ", kitchenEmitter="
                + kitchenEmitter + ", orderUpdateEmitter=" + orderUpdateEmitter + '}';
    }

}
