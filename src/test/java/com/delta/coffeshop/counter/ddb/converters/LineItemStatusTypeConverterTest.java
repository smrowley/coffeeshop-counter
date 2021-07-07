package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.LineItemStatus;

public class LineItemStatusTypeConverterTest {
    private LineItemStatusTypeConverter lineItemStatusTypeConverter;

    @BeforeEach
    public void setup() {
        lineItemStatusTypeConverter = new LineItemStatusTypeConverter();
    }

    @Test
    public void test_convert() {
        assertEquals(LineItemStatus.FULFILLED.toString(),
                lineItemStatusTypeConverter.convert(LineItemStatus.FULFILLED));
        assertEquals(LineItemStatus.IN_PROGRESS.toString(),
                lineItemStatusTypeConverter.convert(LineItemStatus.IN_PROGRESS));
        assertEquals(LineItemStatus.PLACED.toString(),
                lineItemStatusTypeConverter.convert(LineItemStatus.PLACED));
    }

    @Test
    public void test_unconvert() {
        assertEquals(LineItemStatus.FULFILLED,
                lineItemStatusTypeConverter.unconvert(LineItemStatus.FULFILLED.toString()));
        assertEquals(LineItemStatus.IN_PROGRESS.toString(),
                lineItemStatusTypeConverter.convert(LineItemStatus.IN_PROGRESS));
        assertEquals(LineItemStatus.PLACED.toString(),
                lineItemStatusTypeConverter.convert(LineItemStatus.PLACED));
    }

    @Test
    public void test_unconvert_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            lineItemStatusTypeConverter.unconvert(null);
        });
    }
}
