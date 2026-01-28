package com.mathew.subscribo.subscription.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column (name= "billing_cycle", nullable = false)
    private BillingCycle billingCycle;

    @Column (nullable = false)
    private PlanStatus status;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
