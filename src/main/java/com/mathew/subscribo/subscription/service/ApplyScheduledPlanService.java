package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplyScheduledPlanService {
    private final SubscriptionJpaRepositoryRead subscriptionRead;
    private final SubscriptionJpaRepositoryWrite subscriptionWrite;
    private final SubscriptionChangeJpaRepositoryRead subscriptionChangeRead;
    private final SubscriptionChangeJpaRepositoryWrite subscriptionChangeJpaRepositoryWrite;
    private final Logger log = LoggerFactory.getLogger(ApplyScheduledPlanService.class);

    public ApplyScheduledPlanService(
            SubscriptionJpaRepositoryRead subscriptionRead,
            SubscriptionJpaRepositoryWrite subscriptionWrite,
            SubscriptionChangeJpaRepositoryRead subscriptionChangeRead, SubscriptionChangeJpaRepositoryWrite subscriptionChangeJpaRepositoryWrite
    ) {
        this.subscriptionRead = subscriptionRead;
        this.subscriptionWrite = subscriptionWrite;
        this.subscriptionChangeRead = subscriptionChangeRead;
        this.subscriptionChangeJpaRepositoryWrite = subscriptionChangeJpaRepositoryWrite;
    }
    public void execute (){
        List<SubscriptionChangeEntity> pending = subscriptionChangeRead.findAllPending(LocalDateTime.now());

        for (SubscriptionChangeEntity change : pending) {
            try {
                applyChange(change);
            }catch (Exception e){
                log.error("Failed to apply change | changeId={} | error={}", change.getId(), e.getMessage());
            }
        }

    }

    private void applyChange (SubscriptionChangeEntity change){

        SubscriptionEntity subscription = subscriptionRead.findById(change.getSubscriptionId())
                .orElseThrow();

        subscription.setPlanId(change.getNewPlanId());
        subscription.setBillingCycle(change.getNewBillingCycle());
        subscription.setCurrentPrice(change.getNewPrice());

        subscriptionWrite.save(subscription);

        change.markAsApplied();
        subscriptionChangeJpaRepositoryWrite.save(change);

        log.info(
                "Applied scheduled plan change | subscriptionId={} | newPlanId={}",
                subscription.getId(),
                change.getNewPlanId()
        );
    }
}
