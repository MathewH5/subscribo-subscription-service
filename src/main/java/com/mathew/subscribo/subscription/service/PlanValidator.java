package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.ConflictException;
import com.mathew.subscribo.subscription.exception.PlanNotFoundException;
import com.mathew.subscribo.subscription.model.PlanStatus;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.repository.PlanJpaRepositoryRead;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PlanValidator {
    private final PlanJpaRepositoryRead planJpaRepositoryRead;

    public PlanValidator(PlanJpaRepositoryRead planJpaRepositoryRead) {
        this.planJpaRepositoryRead = planJpaRepositoryRead;
    }

    public PlanEntity validate (Long id){
        if (id == null) {
            throw new IllegalArgumentException("O ID do plano não pode ser nulo.");
        }
        return planJpaRepositoryRead.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(id));
    }

    public void validateChange(PlanEntity currentPlan, PlanEntity targetPlan){
        if(!Objects.equals(currentPlan.getProductCode(), targetPlan.getProductCode())){
            throw new ConflictException(
                    "The target plan must belong to the same product."
            );
        }

        if (Objects.equals(currentPlan.getId(), targetPlan.getId())) {
            throw new ConflictException(
                    "The subscription is already on this plan."
            );
        }

        if (targetPlan.getStatus() != PlanStatus.ACTIVE) {
            throw new ConflictException(
                    "The target plan is not active."
            );
        }
    }
}
