package com.delta.coffeeshop.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import com.delta.coffeeshop.counter.domain.valueobjects.TicketUp;

/**
 * Jackson deserializer for TicketUp value object
 */
public class TicketUpDeserializer extends ObjectMapperDeserializer<TicketUp> {

    public TicketUpDeserializer() {
        super(TicketUp.class);
    }
}
