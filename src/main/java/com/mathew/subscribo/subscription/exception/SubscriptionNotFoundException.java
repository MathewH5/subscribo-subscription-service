package com.mathew.subscribo.subscription.exception;

public class SubscriptionNotFoundException extends RuntimeException{
    public SubscriptionNotFoundException(Long customerId) {
        super("Subscription with id " + customerId + " not found");
    }
}
