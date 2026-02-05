package com.mathew.subscribo.subscription.model;

public record ChangeSubscriptionPlanRequest (
        Long planId ,
        BillingCycle scheduledBillingCycle
){
}
