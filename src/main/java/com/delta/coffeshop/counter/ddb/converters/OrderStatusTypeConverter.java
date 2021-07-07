package com.delta.coffeshop.counter.ddb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.delta.coffeeshop.counter.domain.OrderStatus;

public class OrderStatusTypeConverter implements DynamoDBTypeConverter<String, OrderStatus> {

    @Override
    public String convert(OrderStatus orderStatus) {
        return orderStatus.toString();
    }

    @Override
    public OrderStatus unconvert(String val) {
        for (OrderStatus v : OrderStatus.values()) {
            if (v.toString().equalsIgnoreCase(val)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
