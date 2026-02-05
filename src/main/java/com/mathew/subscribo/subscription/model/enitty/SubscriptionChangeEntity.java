package com.mathew.subscribo.subscription.model.enitty;

import com.mathew.subscribo.subscription.model.BillingCycle;
import com.mathew.subscribo.subscription.model.ChangeType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class SubscriptionChangeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private ChangeType changeType;

    @Column(name = "old_plan_id", nullable = false)
    private Long oldPlanId;

    @Column(name = "new_plan_id", nullable = false)
    private Long newPlanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_billing_cycle", nullable = false)
    private BillingCycle newBillingCycle;

    @Column(name = "new_price", nullable = false)
    private BigDecimal newPrice;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    public boolean isPending() {
        return appliedAt == null && changedAt.isBefore(LocalDateTime.now());
    }

    public void markAsApplied() {
        this.appliedAt = LocalDateTime.now();
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public BillingCycle getNewBillingCycle() {
        return newBillingCycle;
    }

    public void setNewBillingCycle(BillingCycle newBillingCycle) {
        this.newBillingCycle = newBillingCycle;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
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
