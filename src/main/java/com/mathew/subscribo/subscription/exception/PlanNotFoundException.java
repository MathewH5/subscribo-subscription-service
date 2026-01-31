package com.mathew.subscribo.subscription.exception;

public class PlanNotFoundException extends RuntimeException{
    public PlanNotFoundException(Long customerId) {
        super("Plan with id " + customerId + " not found");
    }
}
