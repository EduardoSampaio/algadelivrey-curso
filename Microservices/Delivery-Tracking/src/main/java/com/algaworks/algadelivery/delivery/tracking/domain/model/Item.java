package com.algaworks.algadelivery.delivery.tracking.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
public class Item {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    @Setter(AccessLevel.PACKAGE)
    private Integer quantity;

    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Delivery delivery;

    static Item brandNew(String name, Integer quantity, Delivery delivery) {
        Item item = new Item();
        item.id = UUID.randomUUID();
        item.name = name;
        item.quantity = quantity;
        item.delivery = delivery;
        return item;
    }
}
