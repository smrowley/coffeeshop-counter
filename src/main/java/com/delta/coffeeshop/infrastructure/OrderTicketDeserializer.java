package com.delta.coffeeshop.infrastructure;

import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderTicketDeserializer extends ObjectMapperDeserializer<OrderTicket> {

    public OrderTicketDeserializer() {
        super(OrderTicket.class);
    }
}
