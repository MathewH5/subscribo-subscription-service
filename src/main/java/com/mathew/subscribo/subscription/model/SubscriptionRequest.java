package com.mathew.subscribo.subscription.model;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SubscriptionRequest(
        @NotNull Long customerId,
        @NotNull Long planId,
        @NotNull SubscriptionStatus status,
        @NotNull BigDecimal currentPrice,
        @NotNull BillingCycle billingCycle
) {}
