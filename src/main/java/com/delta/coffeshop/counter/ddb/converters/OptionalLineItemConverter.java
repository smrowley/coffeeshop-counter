package com.delta.coffeshop.counter.ddb.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.delta.coffeeshop.counter.domain.LineItem;

public class OptionalLineItemConverter
        implements DynamoDBTypeConverter<List<LineItem>, Optional<List<LineItem>>> {

    @Override
    public List<LineItem> convert(Optional<List<LineItem>> lineItemList) {
        if (lineItemList != null)
            return lineItemList.orElse(new ArrayList<LineItem>());
        else
            return new ArrayList<LineItem>();
    }

    @Override
    public Optional<List<LineItem>> unconvert(List<LineItem> lineItemList) {
        return Optional.<List<LineItem>>ofNullable(lineItemList);
    }

}
