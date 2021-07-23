package com.delta.coffeeshop.counter.domain.valueobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.delta.coffeeshop.counter.domain.Order;

/**
 * Value object returned from an Order. Contains the Order aggregate and a List ExportedEvent
 */
public class OrderEventResult {

    private Order order;

    private List<OrderTicket> baristaTickets;

    private List<OrderTicket> kitchenTickets;

    private List<OrderUpdate> orderUpdates;

    public OrderEventResult() {}

    public Order getOrder() {
        return order;
    }


    public void addUpdate(final OrderUpdate orderUpdate) {
        if (this.orderUpdates == null) {
            this.orderUpdates = new ArrayList<>();
        }
        this.orderUpdates.add(orderUpdate);
    }

    public void addBaristaTicket(final OrderTicket orderTicket) {
        if (this.baristaTickets == null) {
            this.baristaTickets = new ArrayList<>();
        }
        this.baristaTickets.add(orderTicket);
    }

    public void addKitchenTicket(final OrderTicket orderTicket) {
        if (this.kitchenTickets == null) {
            this.kitchenTickets = new ArrayList<>();
        }
        this.kitchenTickets.add(orderTicket);
    }

    public Optional<List<OrderTicket>> getBaristaTickets() {
        return Optional.ofNullable(this.baristaTickets);
    }

    public Optional<List<OrderTicket>> getKitchenTickets() {
        return Optional.ofNullable(this.kitchenTickets);
    }

    @Override
    public String toString() {
        return "OrderEventResult{" + "order=" + order + ", baristaTickets=" + baristaTickets
                + ", kitchenTickets=" + kitchenTickets + ", orderUpdates=" + orderUpdates + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderEventResult))
            return false;

        OrderEventResult that = (OrderEventResult) o;

        if (getOrder() != null ? !getOrder().equals(that.getOrder()) : that.getOrder() != null)
            return false;
        if (baristaTickets != null ? !baristaTickets.equals(that.baristaTickets)
                : that.baristaTickets != null)
            return false;
        if (kitchenTickets != null ? !kitchenTickets.equals(that.kitchenTickets)
                : that.kitchenTickets != null)
            return false;
        return orderUpdates != null ? orderUpdates.equals(that.orderUpdates)
                : that.orderUpdates == null;
    }

    @Override
    public int hashCode() {
        int result = getOrder() != null ? getOrder().hashCode() : 0;
        result = 31 * result + (baristaTickets != null ? baristaTickets.hashCode() : 0);
        result = 31 * result + (kitchenTickets != null ? kitchenTickets.hashCode() : 0);
        result = 31 * result + (orderUpdates != null ? orderUpdates.hashCode() : 0);
        return result;
    }


    public void setBaristaTickets(List<OrderTicket> baristaTickets) {
        this.baristaTickets = baristaTickets;
    }

    public void setKitchenTickets(List<OrderTicket> kitchenTickets) {
        this.kitchenTickets = kitchenTickets;
    }

    public List<OrderUpdate> getOrderUpdates() {
        return orderUpdates;
    }

    public void setOrderUpdates(List<OrderUpdate> orderUpdates) {
        this.orderUpdates = orderUpdates;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
