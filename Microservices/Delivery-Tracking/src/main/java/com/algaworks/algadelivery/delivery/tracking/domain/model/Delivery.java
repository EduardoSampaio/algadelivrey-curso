package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
public class Delivery {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private OffsetDateTime placeAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveredAt;
    private OffsetDateTime fulfilledAt;
    private OffsetDateTime deliveredAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    private DeliveryStatus status;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();


    public static Delivery draft(){
        Delivery delivery = new Delivery();
        delivery.id = UUID.randomUUID();
        delivery.status = DeliveryStatus.DRAFT;
        delivery.totalItems = 0;
        delivery.totalCost = BigDecimal.ZERO;
        delivery.courierPayout = BigDecimal.ZERO;
        delivery.distanceFee = BigDecimal.ZERO;

        return delivery;
    }

    public UUID addItem(String itemName, Integer quantity){
        Item item = Item.brandNew(itemName, quantity, this);
        items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId){
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void removeItems(){
        items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails preparationDetails){
        verifyIfCanBeEdited();
        this.sender = preparationDetails.getSender();
        this.recipient = preparationDetails.getRecipient();
        this.distanceFee = preparationDetails.getDistanceFeed();
        this.courierPayout = preparationDetails.getCourierPayout();
        this.expectedDeliveredAt = OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime());
        this.totalCost = this.distanceFee.add(this.courierPayout);
    }

    public void changeItemQuantity(UUID itemId, int quantity){
        Item item = getItems().stream()
                              .filter(item1 -> item1.getId().equals(itemId))
                              .findFirst().orElseThrow();

        item.setQuantity(quantity);
        calculateTotalItems();
    }

    public void place() {
        verifyIfCanBePlaced();

        this.placeAt = OffsetDateTime.now();
        this.status = DeliveryStatus.WAITING_FOR_COURIER;
    }

    public void pickUp(UUID courierId) {
        this.courierId = courierId;
        this.status = DeliveryStatus.IN_TRANSIT;
        this.assignedAt = OffsetDateTime.now();
    }

    public void markAsDelivered() {
        this.status = DeliveryStatus.DELIVERED;
        this.fulfilledAt = OffsetDateTime.now();
    }


    public void calculateTotalItems(){
        setTotalItems(items.stream().mapToInt(Item::getQuantity).sum());
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void verifyIfCanBePlaced(){
        if(!isFilled()){
            throw new DomainException("Delivery cannot be placed without items.");
        }
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException("Delivery cannot be placed in DRAFT status.");
        }
    }

    private boolean isFilled(){
        return this.getSender() != null && this.getRecipient() != null && this.totalCost != null;
    }

    private void verifyIfCanBeEdited(){
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException("Delivery cannot be edited in " + getStatus() + " status.");
        }
    }

    private void changeStatus(DeliveryStatus newStatus){
        if(newStatus != null && this.status.canNotChangeTo(newStatus)){
            throw new DomainException("Delivery cannot change from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails{
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFeed;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }
}
