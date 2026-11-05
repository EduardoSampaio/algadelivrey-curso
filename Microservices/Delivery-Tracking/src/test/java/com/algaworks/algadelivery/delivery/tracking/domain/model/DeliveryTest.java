package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shouldChangeStatusToPlace() {
        Delivery delivery = Delivery.draft();
        delivery.editPreparationDetails(createdValidPreparationDetails());
        delivery.place();
        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlaceAt());
    }

    @Test
    public void shouldNotChangeStatusToNotPlace() {
        Delivery delivery = Delivery.draft();
        assertThrows(DomainException.class, delivery::place);
        assertNull(delivery.getPlaceAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        var sender = ContactPoint.builder()
                .zipCode("12345-678")
                .street("Rua A")
                .number("123")
                .complement("Apto 101")
                .name("João da Silva")
                .phone("(11) 98765-4321")
                .build();

        var receiver = ContactPoint.builder()
                .zipCode("12345-678")
                .street("Rua A")
                .number("123")
                .complement("Apto 101")
                .name("Jose da Silva")
                .phone("(11) 98765-4321")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(receiver)
                .distanceFeed(BigDecimal.valueOf(10.0))
                .courierPayout(BigDecimal.valueOf(5.0))
                .expectedDeliveryTime(java.time.Duration.ofHours(1))
                .build();
    }
}