package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplyScheduledPlanService {

    private final SubscriptionChangeJpaRepositoryRead subscriptionChangeRead;
    private final SubscriptionApplier applier;
    private final Logger log = LoggerFactory.getLogger(ApplyScheduledPlanService.class);

    private final Counter successCounter;
    private final Counter errorCounter;
    private final Timer executionTimer;

    public ApplyScheduledPlanService(
            SubscriptionChangeJpaRepositoryRead subscriptionChangeRead,
            SubscriptionApplier applier,
            MeterRegistry meterRegistry
    ) {
        this.applier = applier;
        this.subscriptionChangeRead = subscriptionChangeRead;

        this.successCounter = Counter.builder("job.apply_scheduled_plan.success")
                .description("Successful execution of ApplyScheduledPlan job")
                .register(meterRegistry);

        this.errorCounter = Counter.builder("job.apply_scheduled_plan.error")
                .description("Feiled executions of ApplyScheduledPlan job")
                .register(meterRegistry);

        this.executionTimer = Timer.builder("job.apply_scheduled_plan.duration")
                .description("Execution time of ApplyScheduledPlan job")
                .register(meterRegistry);
    }

    public void execute() {
        executionTimer.record(this::executeInternal);
    }

    private void executeInternal(){
        List<SubscriptionChangeEntity> pending =
                subscriptionChangeRead.findAllPending(LocalDateTime.now());

        int success = 0;
        int failed = 0;

        for (SubscriptionChangeEntity change : pending) {
            try {
                applier.applyChange(change);
                success++;
            } catch (Exception e) {
                failed++;
                log.error(
                        "Failed to apply change | changeId={} | error={}",
                        change.getId(),
                        e.getMessage(),
                        e
                );
            }
        }

        successCounter.increment(success);
        errorCounter.increment(failed);
    }

}
