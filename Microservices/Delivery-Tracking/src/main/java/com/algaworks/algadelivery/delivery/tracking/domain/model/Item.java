package com.algaworks.algadelivery.delivery.tracking.domain.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter(AccessLevel.PRIVATE)
public class Item {
    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    @Setter(AccessLevel.PACKAGE)
    private Integer quantity;

    static Item brandNew(String name, Integer quantity){
        Item item = new Item();
        item.id = UUID.randomUUID();
        item.name = name;
        item.quantity = quantity;
        return item;
    }
}
