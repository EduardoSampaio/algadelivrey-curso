package com.algaworks.algadelivery.delivery.tracking.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class DeliveryInput {

    @NotNull
    private ContactPointInput sender;
    @NotNull
    private ContactPointInput recipient;
    @NotEmpty
    @Size(min = 1)
    private List<ItemInput> items;
}
