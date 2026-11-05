package com.algaworks.algadelivery.courier.management.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class AssignedDelivery {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private OffsetDateTime assignedAt;

    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Courier courier;

    static AssignedDelivery pending(UUID id) {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.id = id;
        assignedDelivery.assignedAt = OffsetDateTime.now();
        return assignedDelivery;
    }
}
