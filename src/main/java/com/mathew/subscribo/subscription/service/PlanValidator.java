package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.PlanNotFoundException;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.repository.PlanJpaRepositoryRead;

import org.springframework.stereotype.Service;

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

}
