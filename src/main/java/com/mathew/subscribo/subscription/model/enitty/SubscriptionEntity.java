package com.mathew.subscribo.subscription.model.enitty;

import com.mathew.subscribo.subscription.model.BillingCycle;
import com.mathew.subscribo.subscription.model.SubscriptionStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private Long customerId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false, updatable = false)
    private BigDecimal initialPrice;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    @Column(name = "scheduled_plan_id")
    private Long scheduledPlanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scheduled_billing_cycle")
    private BillingCycle scheduledBillingCycle;

    @Column(name = "scheduled_price")
    private BigDecimal scheduledPrice;

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public LocalDateTime getTrialEndsAt() {
        return trialEndsAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public void setNextBillingDate(LocalDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setTrialEndsAt(LocalDateTime trialEndsAt) {
        this.trialEndsAt = trialEndsAt;
    }

    public void setInitialPrice(BigDecimal initialPrice) {
        this.initialPrice = initialPrice;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getScheduledPlanId() {
        return scheduledPlanId;
    }

    public void setScheduledPlanId(Long scheduledPlanId) {
        this.scheduledPlanId = scheduledPlanId;
    }

    public BigDecimal getScheduledPrice() {
        return scheduledPrice;
    }

    public void setScheduledPrice(BigDecimal scheduledPrice) {
        this.scheduledPrice = scheduledPrice;
    }

    public BillingCycle getScheduledBillingCycle() {
        return scheduledBillingCycle;
    }

    public void setScheduledBillingCycle(BillingCycle scheduledBillingCycle) {
        this.scheduledBillingCycle = scheduledBillingCycle;
    }
}
