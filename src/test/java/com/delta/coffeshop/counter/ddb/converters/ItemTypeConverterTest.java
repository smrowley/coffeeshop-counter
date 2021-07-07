package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.Item;

public class ItemTypeConverterTest {
    private ItemTypeConverter itemTypeConverter;

    @BeforeEach
    public void setup() {
        itemTypeConverter = new ItemTypeConverter();
    }

    @Test
    public void test_convert() {
        assertEquals(Item.CAKEPOP.toString(), itemTypeConverter.convert(Item.CAKEPOP));
        assertEquals(Item.CAPPUCCINO.toString(), itemTypeConverter.convert(Item.CAPPUCCINO));
        assertEquals(Item.COFFEE_BLACK.toString(), itemTypeConverter.convert(Item.COFFEE_BLACK));
        assertEquals(Item.COFFEE_WITH_ROOM.toString(),
                itemTypeConverter.convert(Item.COFFEE_WITH_ROOM));
        assertEquals(Item.CROISSANT.toString(), itemTypeConverter.convert(Item.CROISSANT));
        assertEquals(Item.ESPRESSO.toString(), itemTypeConverter.convert(Item.ESPRESSO));
        assertEquals(Item.ESPRESSO_DOUBLE.toString(),
                itemTypeConverter.convert(Item.ESPRESSO_DOUBLE));
        assertEquals(Item.MUFFIN.toString(), itemTypeConverter.convert(Item.MUFFIN));
        assertEquals(Item.LATTE.toString(), itemTypeConverter.convert(Item.LATTE));

    }

    @Test
    public void test_unconvert() {
        assertEquals(Item.CAKEPOP, itemTypeConverter.unconvert(Item.CAKEPOP.toString()));
        assertEquals(Item.CAPPUCCINO.toString(), itemTypeConverter.convert(Item.CAPPUCCINO));
        assertEquals(Item.COFFEE_BLACK.toString(), itemTypeConverter.convert(Item.COFFEE_BLACK));
        assertEquals(Item.COFFEE_WITH_ROOM,
                itemTypeConverter.unconvert(Item.COFFEE_WITH_ROOM.toString()));
        assertEquals(Item.ESPRESSO, itemTypeConverter.unconvert(Item.ESPRESSO.toString()));
        assertEquals(Item.ESPRESSO_DOUBLE,
                itemTypeConverter.unconvert(Item.ESPRESSO_DOUBLE.toString()));
        assertEquals(Item.MUFFIN, itemTypeConverter.unconvert(Item.MUFFIN.toString()));
        assertEquals(Item.LATTE, itemTypeConverter.unconvert(Item.LATTE.toString()));
        assertEquals(Item.CROISSANT, itemTypeConverter.unconvert(Item.CROISSANT.toString()));

    }

    @Test
    public void test_unconvert_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            itemTypeConverter.unconvert(null);
        });
    }
}
