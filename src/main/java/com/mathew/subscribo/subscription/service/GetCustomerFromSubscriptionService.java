package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.client.CustomerClient;
import com.mathew.subscribo.subscription.model.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public class GetCustomerFromSubscriptionService {
    private final CustomerClient client;

    public GetCustomerFromSubscriptionService(CustomerClient client) {
        this.client = client;
    }

    public CustomerResponse execute(long id){
        return client.getCustomer(id);
    }
}
