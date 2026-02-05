package com.mathew.subscribo.subscription.repository;

import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("subscriptionRead")
public interface SubscriptionJpaRepositoryRead extends JpaRepository<SubscriptionEntity, Long> {

}
