package com.delta.coffeeshop.infrastructure;

import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Custom Jackson deserializer for PlaceOrderCommands
 */
public class PlaceOrderCommandDeserializer extends ObjectMapperDeserializer<PlaceOrderCommand> {

    public PlaceOrderCommandDeserializer() {
        super(PlaceOrderCommand.class);
    }

    /*
     * @Override public PlaceOrderCommand deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
     * 
     */
    /*
     * @JsonProperty("id") final String id,
     * 
     * @JsonProperty("orderSource") final OrderSource orderSource,
     * 
     * @JsonProperty("location") final Location location,
     * 
     * @JsonProperty("rewardsId") final String loyaltyMemberId,
     * 
     * @JsonProperty("baristaItems") Optional<List<LineItem>> baristaLineItems,
     * 
     * @JsonProperty("kitchenItems") Optional<List<LineItem>> kitchenLineItems) {
     *//*
        * 
        * 
        * JsonNode node = jp.getCodec().readTree(jp); String id = node.get("id").asText(); OrderSource orderSource = String itemName = node.get("itemName").asText();
        * int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
        * 
        * }
        */
}
