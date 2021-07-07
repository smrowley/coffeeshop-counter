package com.delta.coffeshop.counter.ddb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.delta.coffeeshop.counter.domain.Item;

public class ItemTypeConverter implements DynamoDBTypeConverter<String, Item> {

    @Override
    public String convert(Item item) {
        return item.toString();
    }

    @Override
    public Item unconvert(String val) {
        for (Item v : Item.values()) {
            if (v.toString().equalsIgnoreCase(val)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
