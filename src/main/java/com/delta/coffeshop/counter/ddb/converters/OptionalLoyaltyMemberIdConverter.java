package com.delta.coffeshop.counter.ddb.converters;

import java.util.Optional;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class OptionalLoyaltyMemberIdConverter
        implements DynamoDBTypeConverter<String, Optional<String>> {

    @Override
    public String convert(Optional<String> string) {
        return string.orElse("");
    }

    @Override
    public Optional<String> unconvert(String string) {
        return Optional.ofNullable(string);
    }

}
