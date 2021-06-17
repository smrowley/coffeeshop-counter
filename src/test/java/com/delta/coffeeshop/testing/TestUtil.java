package com.delta.coffeeshop.testing;

import com.delta.coffeeshop.counter.domain.Item;
import com.delta.coffeeshop.counter.domain.LineItem;
import com.delta.coffeeshop.counter.domain.LineItemStatus;
import com.delta.coffeeshop.counter.domain.Location;
import com.delta.coffeeshop.counter.domain.Order;
import com.delta.coffeeshop.counter.domain.OrderSource;
import com.delta.coffeeshop.counter.domain.OrderStatus;
import com.delta.coffeeshop.counter.domain.commands.CommandItem;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.events.OrderCreatedEvent;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderEventResult;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;
import com.delta.coffeeshop.counter.domain.valueobjects.TicketUp;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TestUtil {

    public static PlaceOrderCommand stubPlaceOrderCommand() {
        return new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                OrderSource.WEB,
                Location.ATLANTA,
                UUID.randomUUID().toString(),
                Optional.of(stubSingleBaristaItem()),
                Optional.empty());
    };

    private static List<CommandItem> stubSingleBaristaItem() {
        return Arrays.asList(new CommandItem(Item.COFFEE_BLACK, "Foo", BigDecimal.valueOf(3.25)));
    }

    public static Order stubOrder() {
        Order order = new Order(
                UUID.randomUUID().toString(),
                OrderSource.COUNTER,
                Location.RALEIGH,
                UUID.randomUUID().toString(),
                Instant.now(),
                OrderStatus.PLACED,
                null,
                null);
        order.addBaristaLineItem(new LineItem(Item.COFFEE_BLACK, "Rocky", BigDecimal.valueOf(3.00), LineItemStatus.PLACED, order));
        return order;
    }

    public static OrderEventResult stubOrderEventResult() {

        // create the return value
        OrderEventResult orderEventResult = new OrderEventResult();

        // build the order from the PlaceOrderCommand
        Order order = new Order(UUID.randomUUID().toString());
        order.setOrderSource(OrderSource.WEB);
        order.setLocation(Location.ATLANTA);
        order.setTimestamp(Instant.now());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderEventResult.setOrder(order);
        orderEventResult.setBaristaTickets(TestUtil.stubBaristaTickets());
        orderEventResult.setOutboxEvents(mockOrderInEvent());
        return orderEventResult;
    }

    private static List<ExportedEvent> mockOrderInEvent() {
        return Arrays.asList(OrderCreatedEvent.of(stubOrder()));
    }

    private static List<OrderTicket> stubBaristaTickets() {
        return Arrays.asList(new OrderTicket(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.COFFEE_BLACK, "Rocky"));
    }

    public static TicketUp stubOrderTicketUp() {

        return new TicketUp(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Item.COFFEE_BLACK,
                "Capt. Kirk",
                "Mr. Spock"
        );
    }

    public static TicketUp stubOrderTicketUp(String orderId) {

        return new TicketUp(
                orderId,
                UUID.randomUUID().toString(),
                Item.COFFEE_BLACK,
                "Capt. Kirk",
                "Mr. Spock"
        );
    }
}
