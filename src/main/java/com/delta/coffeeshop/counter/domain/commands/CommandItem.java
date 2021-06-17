package com.delta.coffeeshop.counter.domain.commands;

import com.delta.coffeeshop.counter.domain.Item;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.StringJoiner;

@RegisterForReflection
public class CommandItem {

    public final Item item;

    public final String name;

    public final BigDecimal price;

    public CommandItem(Item item, String name, BigDecimal price) {
        this.item = item;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommandItem.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("name='" + name + "'")
                .add("price=" + price)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandItem that = (CommandItem) o;

        if (item != that.item) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return price != null ? price.equals(that.price) : that.price == null;
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    public Item getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
