package com.delta.coffeeshop.counter.domain;

import java.math.BigDecimal;
import java.util.UUID;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.delta.coffeshop.counter.ddb.converters.ItemTypeConverter;
import com.delta.coffeshop.counter.ddb.converters.LineItemStatusTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"orderId"})
@DynamoDBDocument
public class LineItem {

    private String itemId;
    private Item item;
    private String name;
    private BigDecimal price;
    private LineItemStatus lineItemStatus;

    public LineItem() {
        this.itemId = UUID.randomUUID().toString();
    }

    public LineItem(Item item, String name, BigDecimal price, LineItemStatus lineItemStatus) {
        this.itemId = UUID.randomUUID().toString();
        this.item = item;
        this.name = name;
        this.price = price;
        this.lineItemStatus = lineItemStatus;
    }

    @DynamoDBTypeConverted(converter = ItemTypeConverter.class)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @DynamoDBAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBTypeConverted(converter = LineItemStatusTypeConverter.class)
    public LineItemStatus getLineItemStatus() {
        return lineItemStatus;
    }

    public void setLineItemStatus(LineItemStatus lineItemStatus) {
        this.lineItemStatus = lineItemStatus;
    }

    @DynamoDBAttribute
    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @DynamoDBAttribute
    public BigDecimal getPrice() {
        return this.item.getPrice();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
