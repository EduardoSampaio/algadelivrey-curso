package com.algaworks.algadelivery.courier.management.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
public class Courier {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;
    @Setter(AccessLevel.PUBLIC)
    private String phone;

    private Integer fulfilledDeliveriesQuantity;
    private Integer pendingDeliveriesQuantity;

    private OffsetDateTime lastFulfilledDeliveryAt;

    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries() {
        return Collections.unmodifiableList(pendingDeliveries);
    }

    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.id = UUID.randomUUID();
        courier.name = name;
        courier.phone = phone;
        courier.fulfilledDeliveriesQuantity = 0;
        courier.pendingDeliveriesQuantity = 0;
        return courier;
    }

    public void assign(UUID deliveryId) {
        this.pendingDeliveries.add(AssignedDelivery.pending(deliveryId));
        this.pendingDeliveriesQuantity++;
    }

    public void fulfill(UUID deliveryId) {
        this.pendingDeliveries.removeIf(d -> d.getId().equals(deliveryId));
        this.pendingDeliveriesQuantity--;
        this.fulfilledDeliveriesQuantity++;
        this.lastFulfilledDeliveryAt = OffsetDateTime.now();
    }
}
