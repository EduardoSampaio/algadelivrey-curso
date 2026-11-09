package com.algaworks.algadelivery.delivery.tracking.domain.service;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.NotFoundException;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCheckpointService {
    private final DeliveryRepository deliveryRepository;

    public void place(UUID deliveryId) {
        Delivery delivery = deliveryRepository
                .findById(deliveryId).orElseThrow(
                        () -> new NotFoundException("Delivery not found")
                );
        delivery.place();
        deliveryRepository.saveAndFlush(delivery);
    }

    public void pickup(UUID deliveryId, UUID courierID) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NotFoundException("Delivery not found")
        );
        delivery.pickUp(courierID);
        deliveryRepository.saveAndFlush(delivery);
    }

    public void complete(UUID deliveryId, UUID courierID) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NotFoundException("Delivery not found")
        );
        delivery.markAsDelivered();
        deliveryRepository.saveAndFlush(delivery);
    }
}

