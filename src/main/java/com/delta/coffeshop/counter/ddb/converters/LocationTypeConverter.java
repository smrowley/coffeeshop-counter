package com.delta.coffeshop.counter.ddb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.delta.coffeeshop.counter.domain.Location;

public class LocationTypeConverter implements DynamoDBTypeConverter<String, Location> {

    @Override
    public String convert(Location location) {
        return location.toString();
    }

    @Override
    public Location unconvert(String val) {
        for (Location v : Location.values()) {
            if (v.toString().equalsIgnoreCase(val)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
