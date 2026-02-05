package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.SubscriptionRequest;
import com.mathew.subscribo.subscription.model.SubscriptionResponse;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CreateSubscriptionService {
    private final SubscriptionJpaRepositoryWrite write;
    private final SubscriptionMapper mapper;
    private final PlanValidator planValidator;


    public CreateSubscriptionService(SubscriptionJpaRepositoryWrite write, SubscriptionMapper mapper, PlanValidator planValidator) {
        this.write = write;
        this.mapper = mapper;
        this.planValidator = planValidator;
    }

    public SubscriptionResponse execute(SubscriptionRequest request){
        PlanEntity plan = planValidator.validate(request.planId());

        SubscriptionEntity entity = mapper.toEntity(request);

        entity.setScheduledPrice(plan.getPrice());
        SubscriptionEntity saved = write.save(entity);

        return mapper.toResponse(saved);
    }
}
