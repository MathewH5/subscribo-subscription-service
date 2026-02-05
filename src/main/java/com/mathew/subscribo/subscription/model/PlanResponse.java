package com.mathew.subscribo.subscription.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlanResponse (
        Long id,
        String name,
        String description,
        BigDecimal price,
        BillingCycle billingCycle,
        PlanStatus status,
        LocalDateTime createdAt
){
}
