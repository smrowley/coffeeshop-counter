package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.OrderStatus;

public class OrderStatusTypeConverterTest {
    private OrderStatusTypeConverter orderStatusTypeConverter;

    @BeforeEach
    public void setup() {
        orderStatusTypeConverter = new OrderStatusTypeConverter();
    }

    @Test
    public void test_convert() {
        assertEquals(OrderStatus.FULFILLED.toString(),
                orderStatusTypeConverter.convert(OrderStatus.FULFILLED));
        assertEquals(OrderStatus.IN_PROGRESS.toString(),
                orderStatusTypeConverter.convert(OrderStatus.IN_PROGRESS));
        assertEquals(OrderStatus.PLACED.toString(),
                orderStatusTypeConverter.convert(OrderStatus.PLACED));
    }

    @Test
    public void test_unconvert() {
        assertEquals(OrderStatus.FULFILLED,
                orderStatusTypeConverter.unconvert(OrderStatus.FULFILLED.toString()));
        assertEquals(OrderStatus.IN_PROGRESS.toString(),
                orderStatusTypeConverter.convert(OrderStatus.IN_PROGRESS));
        assertEquals(OrderStatus.PLACED.toString(),
                orderStatusTypeConverter.convert(OrderStatus.PLACED));
    }

    @Test
    public void test_unconvert_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            orderStatusTypeConverter.unconvert(null);
        });
    }
}
