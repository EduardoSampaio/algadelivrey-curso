package com.algaworks.algadelivery.delivery.tracking.infrastructure.fake;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.DeliveryEstimate;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryTimeEstimationServiceFakeImpl
    implements DeliveryTimeEstimationService {
    @Override
    public DeliveryEstimate estimate(ContactPoint sender, ContactPoint recipient) {
        return new DeliveryEstimate(java.time.Duration.ofMinutes(30), 10.0);
    }
}
