package com.delta.coffeshop.counter.ddb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.delta.coffeeshop.counter.domain.OrderSource;

public class OrderSourceTypeConverter implements DynamoDBTypeConverter<String, OrderSource> {

    @Override
    public String convert(OrderSource orderSource) {
        return orderSource.toString();
    }

    @Override
    public OrderSource unconvert(String val) {
        for (OrderSource v : OrderSource.values()) {
            if (v.toString().equalsIgnoreCase(val)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
