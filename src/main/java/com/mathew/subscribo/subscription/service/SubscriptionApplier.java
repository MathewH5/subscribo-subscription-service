package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionApplier {

    private final SubscriptionJpaRepositoryRead subscriptionRead;
    private final SubscriptionJpaRepositoryWrite subscriptionWrite;
    private final SubscriptionChangeJpaRepositoryWrite subscriptionChangeJpaRepositoryWrite;
    private final Logger log = LoggerFactory.getLogger(ApplyScheduledPlanService.class);

    public SubscriptionApplier(SubscriptionJpaRepositoryRead subscriptionRead, SubscriptionJpaRepositoryWrite subscriptionWrite, SubscriptionChangeJpaRepositoryWrite subscriptionChangeJpaRepositoryWrite) {
        this.subscriptionRead = subscriptionRead;
        this.subscriptionWrite = subscriptionWrite;
        this.subscriptionChangeJpaRepositoryWrite = subscriptionChangeJpaRepositoryWrite;
    }

    @Transactional
    public void applyChange(SubscriptionChangeEntity change) {

        SubscriptionEntity subscription = subscriptionRead.findById(change.getSubscriptionId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Subscription not found for changeId=" + change.getId()
                        )
                );

        subscription.setPlanId(change.getNewPlanId());
        subscription.setBillingCycle(change.getNewBillingCycle());
        subscription.setCurrentPrice(change.getNewPrice());

        subscriptionWrite.save(subscription);

        if (change.getAppliedAt() != null) {
            log.warn("Change already applied | changeId={}", change.getId());
            return;
        }

        change.markAsApplied();
        subscriptionChangeJpaRepositoryWrite.save(change);

        log.info(
                "Applied scheduled plan change | subscriptionId={} | newPlanId={}",
                subscription.getId(),
                change.getNewPlanId()
        );
    }
}
