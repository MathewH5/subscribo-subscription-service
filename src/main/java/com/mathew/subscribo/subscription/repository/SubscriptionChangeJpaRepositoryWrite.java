package com.mathew.subscribo.subscription.repository;

import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("subscriptionChangeWrite")
public interface SubscriptionChangeJpaRepositoryWrite extends JpaRepository<SubscriptionChangeEntity, Long> {
}
