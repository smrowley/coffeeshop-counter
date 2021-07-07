package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.OrderSource;

public class OrderSourceTypeConverterTest {
    private OrderSourceTypeConverter orderSourceTypeConverter;

    @BeforeEach
    public void setup() {
        orderSourceTypeConverter = new OrderSourceTypeConverter();
    }

    @Test
    public void test_convert() {
        assertEquals(OrderSource.COUNTER.toString(),
                orderSourceTypeConverter.convert(OrderSource.COUNTER));
        assertEquals(OrderSource.PARTNER.toString(),
                orderSourceTypeConverter.convert(OrderSource.PARTNER));
        assertEquals(OrderSource.WEB.toString(), orderSourceTypeConverter.convert(OrderSource.WEB));
    }

    @Test
    public void test_unconvert() {
        assertEquals(OrderSource.COUNTER,
                orderSourceTypeConverter.unconvert(OrderSource.COUNTER.toString()));
        assertEquals(OrderSource.PARTNER.toString(),
                orderSourceTypeConverter.convert(OrderSource.PARTNER));
        assertEquals(OrderSource.WEB.toString(), orderSourceTypeConverter.convert(OrderSource.WEB));
    }

    @Test
    public void test_unconvert_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            orderSourceTypeConverter.unconvert(null);
        });
    }
}
