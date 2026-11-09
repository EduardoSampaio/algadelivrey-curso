package com.algaworks.algadelivery.courier.management.api.controller;


import com.algaworks.algadelivery.courier.management.api.model.CourierPayoutResultModel;
import com.algaworks.algadelivery.courier.management.api.model.CourierPayoutCalculationInput;
import com.algaworks.algadelivery.courier.management.domain.model.Courier;
import com.algaworks.algadelivery.courier.management.domain.repository.CourierRepository;
import com.algaworks.algadelivery.courier.management.domain.service.CourierPayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/couriers")
public class CourierController {

    private CourierRepository courierRepository;
    private CourierPayoutService courierPayoutService;

    @GetMapping
    public PagedModel<Courier> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<>(courierRepository.findAll(pageable));
    }

    @GetMapping("/{courierId}")
    public Courier findById(@PathVariable UUID courierId) {
        return courierRepository.findById(courierId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Courier not found"));
    }

    @PostMapping("/payout-calculation")
    public CourierPayoutResultModel calculate(@RequestBody CourierPayoutCalculationInput input) {
        BigDecimal payoutFee =  courierPayoutService.calculate(input.getDistanceInKm());

        return new CourierPayoutResultModel(payoutFee);
    }
}
