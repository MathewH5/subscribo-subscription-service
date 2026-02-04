package com.mathew.subscribo.subscription.mapper;

import com.mathew.subscribo.subscription.model.BillingCycle;
import com.mathew.subscribo.subscription.model.SubscriptionRequest;
import com.mathew.subscribo.subscription.model.SubscriptionResponse;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SubscriptionMapper {

    public SubscriptionEntity toEntity(SubscriptionRequest request){
        if (request == null) {
            return null;
        }

        SubscriptionEntity entity = new SubscriptionEntity();

        entity.setCustomerId(request.customerId());
        entity.setPlanId(request.planId());
        entity.setStatus(request.status());
        entity.setCurrentPrice(request.currentPrice());
        entity.setBillingCycle(request.billingCycle());

        LocalDateTime now = LocalDateTime.now();
        entity.setStartDate(now);

        if (request.billingCycle() == BillingCycle.MONTHLY) {
            entity.setEndDate(now.plusMonths(1)); // Adiciona 1 mês exato (ex: 31/jan -> 28/fev)
        } else if (request.billingCycle() == BillingCycle.YEARLY) {
            entity.setEndDate(now.plusYears(1));
        }
        entity.setNextBillingDate(entity.getEndDate());

        entity.setInitialPrice(request.currentPrice());

        return entity;
    }

    public SubscriptionResponse toResponse(SubscriptionEntity entity){
        if (entity == null) {
            return null;
        }
        return new SubscriptionResponse(
                entity.getId(),
                entity.getCustomerId(),
                entity.getPlanId(),
                entity.getStatus(),
                entity.getInitialPrice(),
                entity.getCurrentPrice(),
                entity.getBillingCycle(),
                entity.getScheduledPlanId(),
                entity.getScheduledBillingCycle(),
                entity.getScheduledPrice(),
                entity.getTrialEndsAt(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNextBillingDate(),
                entity.getCanceledAt(),
                entity.getCreatedAt()
        );
    }
}
