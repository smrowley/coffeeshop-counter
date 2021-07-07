package com.delta.coffeshop.counter.ddb.converters;

import java.time.Instant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class InstantTypeConverter implements DynamoDBTypeConverter<String, Instant> {
    @Override
    public String convert(Instant instant) {
        return instant.toString();
    }

    @Override
    public Instant unconvert(String iso8601Time) {
        return Instant.parse(iso8601Time);
    }
}
