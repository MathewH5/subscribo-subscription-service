package com.mathew.subscribo.subscription.controller;

import com.mathew.subscribo.subscription.model.ChangeSubscriptionPlanRequest;
import com.mathew.subscribo.subscription.model.CustomerResponse;
import com.mathew.subscribo.subscription.model.SubscriptionRequest;
import com.mathew.subscribo.subscription.model.SubscriptionResponse;
import com.mathew.subscribo.subscription.service.ChangeSubscriptionPlanService;
import com.mathew.subscribo.subscription.service.CreateSubscriptionService;
import com.mathew.subscribo.subscription.service.GetCustomerFromSubscriptionService;
import com.mathew.subscribo.subscription.service.GetSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final GetSubscriptionService getSubscription;
    private final CreateSubscriptionService createSubscription;
    private final GetCustomerFromSubscriptionService getCustomerFromSubscription;
    private final ChangeSubscriptionPlanService changeSubscriptionPlan;

    public SubscriptionController(GetSubscriptionService getSubscriptionService, CreateSubscriptionService createSubscription, GetCustomerFromSubscriptionService getCustomerFromSubscriptionService, ChangeSubscriptionPlanService changeSubscriptionPlanService) {
        this.getSubscription = getSubscriptionService;
        this.createSubscription = createSubscription;
        this.getCustomerFromSubscription = getCustomerFromSubscriptionService;
        this.changeSubscriptionPlan = changeSubscriptionPlanService;
    }

    @GetMapping
    public List<SubscriptionResponse> getSubscription(){
        return getSubscription.execute();
    }

    @GetMapping("/{customerid}")
    public CustomerResponse getCustomerFromSubscription(@PathVariable Long customerid){
        return getCustomerFromSubscription.execute(customerid);
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest request){
        SubscriptionResponse created = createSubscription.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{subscriptionId}/plan")
    public ResponseEntity<SubscriptionResponse> updateSubscriptionPlan(@PathVariable Long subscriptionId, @RequestBody ChangeSubscriptionPlanRequest request){
        SubscriptionResponse update = changeSubscriptionPlan.execute(subscriptionId, request);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }
}
