package com.mathew.subscribo.subscription.controller;

import com.mathew.subscribo.subscription.model.PlanResponse;
import com.mathew.subscribo.subscription.service.GetPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanController {

    private final GetPlanService getPlanService;

    public PlanController(GetPlanService getPlanService) {
        this.getPlanService = getPlanService;
    }

    @GetMapping
    public List<PlanResponse> getPlans(){
        return getPlanService.getPlans();
    }

    @GetMapping({"/active"})
    public List<PlanResponse> getPlanActive(){
        return getPlanService.listActivePlans();
    }

    @GetMapping("/{id}")
    public PlanResponse getPlanId(@PathVariable Long id){
        return getPlanService.getPlanById(id);
    }

}
