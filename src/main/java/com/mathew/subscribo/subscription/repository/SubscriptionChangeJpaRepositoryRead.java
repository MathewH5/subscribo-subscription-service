package com.mathew.subscribo.subscription.repository;

import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("subscriptionChangeRead")
public interface SubscriptionChangeJpaRepositoryRead extends JpaRepository<SubscriptionChangeEntity, Long> {
    @Query("SELECT s FROM SubscriptionChangeEntity s WHERE s.appliedAt IS NULL AND s.changedAt < :now")
    List<SubscriptionChangeEntity> findAllPending(@Param("now") LocalDateTime now);
}
