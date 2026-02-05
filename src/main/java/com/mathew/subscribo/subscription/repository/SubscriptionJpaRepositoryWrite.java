package com.mathew.subscribo.subscription.repository;

import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("subscriptionWrite")
public interface SubscriptionJpaRepositoryWrite extends JpaRepository<SubscriptionEntity, Long> {
}
