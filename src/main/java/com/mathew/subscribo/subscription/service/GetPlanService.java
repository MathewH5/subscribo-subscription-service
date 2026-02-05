package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.PlanNotFoundException;
import com.mathew.subscribo.subscription.mapper.PlanMapper;
import com.mathew.subscribo.subscription.model.PlanResponse;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.repository.PlanJpaRepositoryRead;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mathew.subscribo.subscription.model.PlanStatus.ACTIVE;

@Service
public class GetPlanService {

    private final PlanMapper mapper;
    private final PlanJpaRepositoryRead planJpaRepositoryRead;

    public GetPlanService(PlanMapper mapper, PlanJpaRepositoryRead planJpaRepositoryRead) {
        this.mapper = mapper;
        this.planJpaRepositoryRead = planJpaRepositoryRead;
    }

    public List<PlanResponse> getPlans(){
        return planJpaRepositoryRead.findAll()
                .stream()
                .map(this::toDto)
                .toList();

    }

    public List<PlanResponse> listActivePlans(){
        return planJpaRepositoryRead.findByStatus(ACTIVE)
                .stream()
                .map(this::toDto)
                .toList();

    }

    public PlanResponse getPlanById(Long id){
        PlanEntity entity = planJpaRepositoryRead.findById(id)
                .orElseThrow(()->new PlanNotFoundException(id));

        return toDto(entity);
    }

    private PlanResponse toDto (PlanEntity entity){
        return mapper.toResponse(entity);
    }
}
