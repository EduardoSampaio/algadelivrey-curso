package com.algaworks.algadelivery.delivery.tracking.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Embeddable
@NoArgsConstructor
@Getter
public class ContactPoint {
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String name;
    private String phone;
}
