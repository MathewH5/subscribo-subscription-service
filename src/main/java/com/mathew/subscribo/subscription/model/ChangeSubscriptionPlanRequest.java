package com.mathew.subscribo.subscription.model;

import java.math.BigDecimal;

public record ChangeSubscriptionPlanRequest (
        Long planId ,
        BillingCycle scheduledBillingCycle
){
}
