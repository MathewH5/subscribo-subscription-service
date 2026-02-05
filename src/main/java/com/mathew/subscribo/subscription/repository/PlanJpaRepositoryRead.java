package com.mathew.subscribo.subscription.repository;

import com.mathew.subscribo.subscription.model.PlanStatus;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("planRead")
public interface PlanJpaRepositoryRead extends JpaRepository<PlanEntity, Long> {

    List<PlanEntity> findByStatus(PlanStatus status);
}
