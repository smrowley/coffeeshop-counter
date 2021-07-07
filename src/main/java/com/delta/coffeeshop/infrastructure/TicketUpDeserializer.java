package com.delta.coffeeshop.infrastructure;

import com.delta.coffeeshop.counter.domain.valueobjects.TicketUp;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Jackson deserializer for TicketUp value object
 */
public class TicketUpDeserializer extends ObjectMapperDeserializer<TicketUp> {

    public TicketUpDeserializer() {
        super(TicketUp.class);
    }
}
