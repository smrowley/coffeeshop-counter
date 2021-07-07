package com.delta.coffeeshop.counter.domain.commands;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.delta.coffeeshop.counter.domain.Location;
import com.delta.coffeeshop.counter.domain.OrderSource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlaceOrderCommand {

    private final String id;

    private final OrderSource orderSource;

    private final Location location;

    private final String loyaltyMemberId;

    private final List<CommandItem> baristaLineItems;

    private final List<CommandItem> kitchenLineItems;

    private final Instant timestamp;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PlaceOrderCommand(@JsonProperty("id") final String id,
            @JsonProperty("orderSource") final OrderSource orderSource,
            @JsonProperty("location") final Location location,
            @JsonProperty("loyaltyMemberId") final String loyaltyMemberId,
            @JsonProperty("baristaLineItems") Optional<List<CommandItem>> baristaLineItems,
            @JsonProperty("kitchenLineItems") Optional<List<CommandItem>> kitchenLineItems) {
        this.id = id;
        this.orderSource = orderSource;
        this.location = location;
        this.loyaltyMemberId = loyaltyMemberId;
        this.baristaLineItems = baristaLineItems.orElse(null);
        this.kitchenLineItems = kitchenLineItems.orElse(null);
        this.timestamp = Instant.now();
    }

    @Override
    public String toString() {
        return "PlaceOrderCommand{" + "id='" + id + '\'' + ", orderSource=" + orderSource
                + ", location=" + location + ", loyaltyMemberId='" + loyaltyMemberId + '\''
                + ", baristaLineItems=" + baristaLineItems + ", kitchenLineItems="
                + kitchenLineItems + ", timestamp=" + timestamp + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlaceOrderCommand that = (PlaceOrderCommand) o;
        return Objects.equals(id, that.id) && orderSource == that.orderSource
                && location == that.location
                && Objects.equals(loyaltyMemberId, that.loyaltyMemberId)
                && Objects.equals(baristaLineItems, that.baristaLineItems)
                && Objects.equals(kitchenLineItems, that.kitchenLineItems)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSource, location, loyaltyMemberId, baristaLineItems,
                kitchenLineItems, timestamp);
    }

    public Optional<List<CommandItem>> getBaristaLineItems() {
        return Optional.ofNullable(baristaLineItems);
    }

    public Optional<List<CommandItem>> getKitchenLineItems() {
        return Optional.ofNullable(kitchenLineItems);
    }

    public Optional<String> getLoyaltyMemberId() {
        return Optional.ofNullable(loyaltyMemberId);
    }

    public String getId() {
        return id;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public Location getLocation() {
        return location;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
