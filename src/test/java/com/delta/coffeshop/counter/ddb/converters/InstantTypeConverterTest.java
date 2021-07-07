package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstantTypeConverterTest {

    private static final String TIMESTAMP_STRING = "2021-06-12T10:12:35Z";

    private static final Instant TIMESTAMP = Instant.parse(TIMESTAMP_STRING);

    private InstantTypeConverter instantTypeConverter;

    @BeforeEach
    public void setup() {
        instantTypeConverter = new InstantTypeConverter();
    }

    @Test
    public void test_convert() {
        assertEquals(TIMESTAMP_STRING, instantTypeConverter.convert(TIMESTAMP));
    }

    @Test
    public void test_unconvert() {
        assertEquals(TIMESTAMP, instantTypeConverter.unconvert(TIMESTAMP_STRING));
    }

}
