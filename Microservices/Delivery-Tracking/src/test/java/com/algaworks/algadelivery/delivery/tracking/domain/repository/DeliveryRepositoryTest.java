package com.algaworks.algadelivery.delivery.tracking.domain.repository;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist(){
        Delivery delivery = new Delivery();
        delivery.editPreparationDetails(createdValidPreparationDetails());
        delivery.addItem("PC", 2);
        delivery.addItem("Notebook", 1);

        Delivery persistendDelivery = deliveryRepository.save(delivery);
        assertEquals(1, persistendDelivery.getItems().size());
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