package com.mathew.subscribo.subscription.job;

import com.mathew.subscribo.subscription.service.ApplyScheduledPlanService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ApplyScheduledPlanJob {

    private final ApplyScheduledPlanService applyScheduledPlanService;

    public ApplyScheduledPlanJob(ApplyScheduledPlanService applyScheduledPlanService) {
        this.applyScheduledPlanService = applyScheduledPlanService;
    }

    @Scheduled(cron = "${jobs.apply-scheduled-plan.cron}")
    public void run(){
        applyScheduledPlanService.execute();
    }
}
