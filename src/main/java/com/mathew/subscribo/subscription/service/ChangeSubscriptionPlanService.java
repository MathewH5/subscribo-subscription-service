package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.ConflictException;
import com.mathew.subscribo.subscription.exception.PlanNotFoundException;
import com.mathew.subscribo.subscription.exception.SubscriptionNotFoundException;
import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.ChangeSubscriptionPlanRequest;
import com.mathew.subscribo.subscription.model.ChangeType;
import com.mathew.subscribo.subscription.model.SubscriptionResponse;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.PlanJpaRepositoryRead;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ChangeSubscriptionPlanService {

    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionJpaRepositoryRead subscriptionRepositoryRead;
    private final SubscriptionJpaRepositoryWrite subscriptionRepositoryWrite;
    private final PlanJpaRepositoryRead planRepositoryRead;
    private final SubscriptionChangeJpaRepositoryWrite subscriptionChangeRepositoryWrite;
    private final PlanValidator planValidator;


    public ChangeSubscriptionPlanService(SubscriptionMapper subscriptionMapper, SubscriptionJpaRepositoryRead subscriptionRepositoryRead, SubscriptionJpaRepositoryWrite subscriptionRepositoryWrite, PlanJpaRepositoryRead planRepositoryRead, SubscriptionChangeJpaRepositoryWrite subscriptionChangeRepositoryWrite, PlanValidator planValidator) {
        this.subscriptionMapper = subscriptionMapper;
        this.subscriptionRepositoryRead = subscriptionRepositoryRead;
        this.subscriptionRepositoryWrite = subscriptionRepositoryWrite;
        this.planRepositoryRead = planRepositoryRead;
        this.subscriptionChangeRepositoryWrite = subscriptionChangeRepositoryWrite;
        this.planValidator = planValidator;
    }


    @Transactional
    public SubscriptionResponse execute (Long id, ChangeSubscriptionPlanRequest request){
        SubscriptionEntity entitySubscription = subscriptionRepositoryRead.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));

        PlanEntity entityCurrentPlan = planRepositoryRead.findById(entitySubscription.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException(entitySubscription.getPlanId()));

        PlanEntity entityNewPlan = planRepositoryRead.findById(request.planId())
                .orElseThrow(() -> new PlanNotFoundException(request.planId()));

        planValidator.validateChange(entityCurrentPlan, entityNewPlan);

        return change(entitySubscription, entityNewPlan, request);
    }

    private SubscriptionResponse change(SubscriptionEntity entitySubscription, PlanEntity entityNewPlan, ChangeSubscriptionPlanRequest request){
        // espera o fim do ciruclo para comecar o novo

        entitySubscription.setScheduledPrice(entityNewPlan.getPrice());
        entitySubscription.setScheduledPlanId(entityNewPlan.getId());
        entitySubscription.setScheduledBillingCycle(entityNewPlan.getBillingCycle());

        SubscriptionEntity saved = subscriptionRepositoryWrite.save(entitySubscription);

        SubscriptionChangeEntity subscriptionChangeEntity = createSubscriptionChangeEntity(entitySubscription, entityNewPlan);

        subscriptionChangeRepositoryWrite.save(subscriptionChangeEntity);

        return subscriptionMapper.toResponse(saved);
    }

    private static SubscriptionChangeEntity createSubscriptionChangeEntity(SubscriptionEntity entitySubscription, PlanEntity entityNewPlan) {
        SubscriptionChangeEntity subscriptionChangeEntity = new SubscriptionChangeEntity();
        subscriptionChangeEntity.setChangeType(
                ChangeType.BILLING_CYCLE_CHANGE
        );
        subscriptionChangeEntity.setChangedAt(entitySubscription.getNextBillingDate());
        subscriptionChangeEntity.setSubscriptionId(entitySubscription.getId());
        subscriptionChangeEntity.setOldPlanId(entitySubscription.getPlanId());
        subscriptionChangeEntity.setNewPlanId(entitySubscription.getScheduledPlanId());
        subscriptionChangeEntity.setNewBillingCycle(entityNewPlan.getBillingCycle());
        subscriptionChangeEntity.setNewPrice(entityNewPlan.getPrice());
        return subscriptionChangeEntity;
    }

}
