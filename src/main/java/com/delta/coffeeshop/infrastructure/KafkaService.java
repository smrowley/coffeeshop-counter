package com.delta.coffeeshop.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.valueobjects.TicketUp;
import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    OrderService orderService;

    @Incoming("orders-in")
    @Blocking
    @Transactional
    public void orderIn(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("PlaceOrderCommand received: {}", placeOrderCommand);
        orderService.onOrderIn(placeOrderCommand);
    }

    @Incoming("orders-up")
    @Blocking
    @Transactional
    public void orderUp(final TicketUp ticketUp) {

        logger.debug("TicketUp received: {}", ticketUp);
        orderService.onOrderUp(ticketUp);
    }
}
