package com.mathew.subscribo.subscription.model.enitty;

import com.mathew.subscribo.subscription.model.changeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class SubscriptionChangeEntity {

    @Id
    private Long subscriptionId;

    @Column(name = "change_type")
    private changeType changeType;

    @Column(name = "old_plan_id")
    private Long oldPlanId;

    @Column(name = "new_plan_id")
    private Long newPlanId;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public com.mathew.subscribo.subscription.model.changeType getChangeType() {
        return changeType;
    }

    public void setChangeType(com.mathew.subscribo.subscription.model.changeType changeType) {
        this.changeType = changeType;
    }

    public Long getOldPlanId() {
        return oldPlanId;
    }

    public void setOldPlanId(Long oldPlanId) {
        this.oldPlanId = oldPlanId;
    }

    public Long getNewPlanId() {
        return newPlanId;
    }

    public void setNewPlanId(Long newPlanId) {
        this.newPlanId = newPlanId;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
