package com.mathew.subscribo.subscription.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(

        Long id,
        Long customerId,
        Long planId,

        SubscriptionStatus status,

        BigDecimal initialPrice,
        BigDecimal currentPrice,

        BillingCycle billingCycle,

        Long scheduledPlanId,
        BillingCycle scheduledBillingCycle,
        BigDecimal scheduledPrice,

        LocalDateTime trialEndsAt,

        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime nextBillingDate,

        LocalDateTime canceledAt,
        LocalDateTime createdAt

) {
}
