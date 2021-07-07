package com.delta.coffeshop.counter.ddb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.delta.coffeeshop.counter.domain.LineItemStatus;

public class LineItemStatusTypeConverter implements DynamoDBTypeConverter<String, LineItemStatus> {
    @Override
    public String convert(LineItemStatus lineItemStatus) {
        return lineItemStatus.toString();
    }

    @Override
    public LineItemStatus unconvert(String val) {
        for (LineItemStatus v : LineItemStatus.values()) {
            if (v.toString().equalsIgnoreCase(val)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
