package com.delta.coffeeshop.counter.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Temp table to instantiate the EntityManager used by outbox extension, if no entity is declared it will error out, should be removed when the outbox extension
 * is removed
 * 
 * @author v49693
 *
 */
@Entity
@Table(name = "Temp")
public class Temp {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    public Temp() {
        this.id = UUID.randomUUID().toString();
    }

    public String getItemId() {
        return id;
    }

    public void setItemId(String itemId) {
        this.id = itemId;
    }
}
