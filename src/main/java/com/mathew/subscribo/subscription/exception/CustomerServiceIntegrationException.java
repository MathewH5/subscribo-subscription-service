package com.mathew.subscribo.subscription.exception;

public class CustomerServiceIntegrationException
        extends RuntimeException {

    public CustomerServiceIntegrationException(String message) {
        super(message);
    }

    public CustomerServiceIntegrationException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}