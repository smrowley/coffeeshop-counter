package com.delta.coffeshop.counter.ddb.converters;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.delta.coffeeshop.counter.domain.LineItem;

public class OptionalLineItemTypeConverterTest {
    private OptionalLineItemConverter optionalLineItemConverter;
    List<LineItem> lineItemList = new ArrayList<LineItem>();
    private Optional<List<LineItem>> optionalLineItem = Optional.of(lineItemList);

    @BeforeEach
    public void setup() {
        optionalLineItemConverter = new OptionalLineItemConverter();
        lineItemList.clear();
    }

    @Test
    public void test_convert_empty() {
        lineItemList.clear();
        assertNotNull(optionalLineItemConverter.convert(optionalLineItem));
    }

    @Test
    public void test_convert_non_empty() {
        lineItemList.add(new LineItem());
        assertNotNull(optionalLineItemConverter.convert(optionalLineItem));
    }

    @Test
    public void test_convert_null() {
        assertNotNull(optionalLineItemConverter.convert(null));
    }

    @Test
    public void test_unconvert_non_empty() {
        lineItemList.add(new LineItem());
        assertNotNull(optionalLineItemConverter.unconvert(lineItemList));
    }

    @Test
    public void test_unconvert_empty() {
        assertNotNull(optionalLineItemConverter.unconvert(lineItemList));
    }

    @Test
    public void test_unconvert_null() {
        lineItemList.add(new LineItem());
        assertNotNull(optionalLineItemConverter.unconvert(null));
    }
}
