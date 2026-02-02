package com.mathew.subscribo.subscription.client;

import com.mathew.subscribo.subscription.model.CustomerResponse;

public interface CustomerClient {
    CustomerResponse getCustomer(Long customerId);
}
