package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.Location;

public class LocationTypeConverterTest {
    private LocationTypeConverter locationTypeConverter;

    @BeforeEach
    public void setup() {
        locationTypeConverter = new LocationTypeConverter();
    }

    @Test
    public void test_convert() {
        assertEquals(Location.ATLANTA.toString(), locationTypeConverter.convert(Location.ATLANTA));
        assertEquals(Location.CHARLOTTE.toString(),
                locationTypeConverter.convert(Location.CHARLOTTE));
        assertEquals(Location.RALEIGH.toString(), locationTypeConverter.convert(Location.RALEIGH));
    }

    @Test
    public void test_unconvert() {
        assertEquals(Location.ATLANTA,
                locationTypeConverter.unconvert(Location.ATLANTA.toString()));
        assertEquals(Location.CHARLOTTE.toString(),
                locationTypeConverter.convert(Location.CHARLOTTE));
        assertEquals(Location.RALEIGH.toString(), locationTypeConverter.convert(Location.RALEIGH));
    }

    @Test
    public void test_unconvert_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            locationTypeConverter.unconvert(null);
        });
    }
}
