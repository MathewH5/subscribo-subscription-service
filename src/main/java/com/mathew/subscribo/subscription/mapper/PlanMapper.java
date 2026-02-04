package com.mathew.subscribo.subscription.mapper;

import com.mathew.subscribo.subscription.model.PlanResponse;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    public PlanResponse toResponse (PlanEntity entity){
        if (entity == null) {
            return null;
        }
        return new PlanResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getBillingCycle(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
