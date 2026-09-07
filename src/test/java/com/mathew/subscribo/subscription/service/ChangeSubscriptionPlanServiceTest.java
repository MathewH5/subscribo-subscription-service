package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.SubscriptionNotFoundException;
import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.ChangeSubscriptionPlanRequest;
import com.mathew.subscribo.subscription.model.ChangeType;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.PlanJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.mathew.subscribo.subscription.model.BillingCycle.MONTHLY;
import static com.mathew.subscribo.subscription.model.BillingCycle.YEARLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeSubscriptionPlanServiceTest {

    @Spy
    private SubscriptionMapper subscriptionMapper = new SubscriptionMapper();

    @Mock
    private SubscriptionJpaRepositoryRead subscriptionRepositoryRead;

    @Mock
    private SubscriptionJpaRepositoryWrite subscriptionRepositoryWrite;

    @Mock
    private PlanJpaRepositoryRead planRepositoryRead;

    @Mock
    private SubscriptionChangeJpaRepositoryWrite subscriptionChangeRepositoryWrite;

    @Mock
    private PlanValidator planValidator;

    @InjectMocks
    private ChangeSubscriptionPlanService changeSubscriptionPlanService;

    @Test
    void shouldThrowExceptionWhenSubscriptionNotFound() {
        Long subscriptionId = 1L;
        ChangeSubscriptionPlanRequest request =
                new ChangeSubscriptionPlanRequest(2L);

        when(subscriptionRepositoryRead.findById(subscriptionId))
                .thenReturn(Optional.empty());

        assertThrows(
                SubscriptionNotFoundException.class,
                () -> changeSubscriptionPlanService.execute(subscriptionId, request)
        );
    }

    @Test
    void shouldSchedulePlanChangeWhenTargetPlanIsValid() {
        Long subscriptionId = 1L;
        LocalDateTime nextBillingDate = LocalDateTime.of(2026, 10, 7, 12, 0);

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setPlanId(1L);
        subscription.setCurrentPrice(BigDecimal.valueOf(45.90));
        subscription.setBillingCycle(MONTHLY);
        subscription.setNextBillingDate(nextBillingDate);

        PlanEntity currentPlan = new PlanEntity();
        currentPlan.setId(1L);
        currentPlan.setPrice(BigDecimal.valueOf(45.90));
        currentPlan.setBillingCycle(MONTHLY);

        PlanEntity targetPlan = new PlanEntity();
        targetPlan.setId(2L);
        targetPlan.setPrice(BigDecimal.valueOf(440.60));
        targetPlan.setBillingCycle(YEARLY);

        ChangeSubscriptionPlanRequest request =
                new ChangeSubscriptionPlanRequest(2L);

        when(subscriptionRepositoryRead.findById(subscriptionId))
                .thenReturn(Optional.of(subscription));

        when(planRepositoryRead.findById(1L))
                .thenReturn(Optional.of(currentPlan));

        when(planRepositoryRead.findById(2L))
                .thenReturn(Optional.of(targetPlan));

        when(subscriptionRepositoryWrite.save(any(SubscriptionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(subscriptionChangeRepositoryWrite.save(any(SubscriptionChangeEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        changeSubscriptionPlanService.execute(subscriptionId, request);

        verify(planValidator).validateChange(currentPlan, targetPlan);

        ArgumentCaptor<SubscriptionEntity> subscriptionCaptor =
                ArgumentCaptor.forClass(SubscriptionEntity.class);

        verify(subscriptionRepositoryWrite).save(subscriptionCaptor.capture());

        SubscriptionEntity savedSubscription = subscriptionCaptor.getValue();

        assertEquals(1L, savedSubscription.getPlanId());
        assertEquals(BigDecimal.valueOf(45.90), savedSubscription.getCurrentPrice());

        assertEquals(2L, savedSubscription.getScheduledPlanId());
        assertEquals(YEARLY, savedSubscription.getScheduledBillingCycle());
        assertEquals(BigDecimal.valueOf(440.60), savedSubscription.getScheduledPrice());

        ArgumentCaptor<SubscriptionChangeEntity> changeCaptor =
                ArgumentCaptor.forClass(SubscriptionChangeEntity.class);

        verify(subscriptionChangeRepositoryWrite).save(changeCaptor.capture());

        SubscriptionChangeEntity savedChange = changeCaptor.getValue();

        assertEquals(1L, savedChange.getOldPlanId());
        assertEquals(2L, savedChange.getNewPlanId());
        assertEquals(YEARLY, savedChange.getNewBillingCycle());
        assertEquals(BigDecimal.valueOf(440.60), savedChange.getNewPrice());
        assertEquals(nextBillingDate, savedChange.getChangedAt());
        assertEquals(ChangeType.BILLING_CYCLE_CHANGE, savedChange.getChangeType());
    }
}