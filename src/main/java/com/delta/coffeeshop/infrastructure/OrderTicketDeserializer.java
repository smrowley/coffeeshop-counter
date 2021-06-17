package com.delta.coffeeshop.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;

public class OrderTicketDeserializer  extends ObjectMapperDeserializer<OrderTicket> {

    public OrderTicketDeserializer() {
        super(OrderTicket.class);
    }
}
