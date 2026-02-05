package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.SubscriptionNotFoundException;
import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.ChangeSubscriptionPlanRequest;
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
import java.util.Optional;

import static com.mathew.subscribo.subscription.model.BillingCycle.MONTHLY;
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
    private SubscriptionChangeJpaRepositoryWrite subscriptionChangeJpaRepositoryWrite;

    @Mock
    private SubscriptionJpaRepositoryRead subscriptionRepositoryRead;

    @Mock
    private SubscriptionJpaRepositoryWrite subscriptionRepositoryWrite;

    @Mock
    private PlanJpaRepositoryRead planRepositoryRead;

    @InjectMocks
    private ChangeSubscriptionPlanService changeSubscriptionPlanService;

    @Test
    void shouldThrowExceptionWhenSubscriptionNotFound(){
        // GIVEN
        Long subscriptionId = 1L;

        ChangeSubscriptionPlanRequest request =
                new ChangeSubscriptionPlanRequest(2L, null);


        when(subscriptionRepositoryRead.findById(subscriptionId))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(
                SubscriptionNotFoundException.class,
                () -> changeSubscriptionPlanService.execute(subscriptionId, request)
        );
    }

    @Test
    void  shouldUpgradeSubscriptionImmediatelyWhenNewPlanIsMoreExpensive(){
        Long subscriptionId = 1L;

        // subscription atual
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setPlanId(1L);
        subscription.setCurrentPrice(BigDecimal.valueOf(10));

        // Plano atual
        PlanEntity currentPlan = new PlanEntity();
        currentPlan.setId(1L);
        currentPlan.setPrice(BigDecimal.valueOf(10));
        currentPlan.setBillingCycle(MONTHLY);


        // novo plano (mais caro)
        PlanEntity newPlan = new PlanEntity();
        newPlan.setId(2L);
        newPlan.setPrice(BigDecimal.valueOf(20));
        newPlan.setBillingCycle(MONTHLY);


        ChangeSubscriptionPlanRequest request =
                new ChangeSubscriptionPlanRequest(2L, MONTHLY);

        when(subscriptionRepositoryRead.findById(subscriptionId))
                .thenReturn(Optional.of(subscription));

        when(planRepositoryRead.findById(1L))
                .thenReturn(Optional.of(currentPlan));

        when(planRepositoryRead.findById(2L))
                .thenReturn(Optional.of(newPlan));

        when(subscriptionRepositoryWrite.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        changeSubscriptionPlanService.execute(subscriptionId, request);

        ArgumentCaptor<SubscriptionEntity> captor = ArgumentCaptor.forClass(SubscriptionEntity.class);
        verify(subscriptionRepositoryWrite).save(captor.capture());

        SubscriptionEntity entitySalva = captor.getValue();

        assertEquals(2L, entitySalva.getPlanId());
        assertEquals(BigDecimal.valueOf(20), entitySalva.getCurrentPrice());

    }

    @Test
    void shouldSchedulePlanChangeWhenNewPlanIsCheaper(){
        Long subscriptionId = 1L;

        // subscription atual
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setPlanId(2L);
        subscription.setCurrentPrice(BigDecimal.valueOf(20));

        // Plano atual
        PlanEntity currentPlan = new PlanEntity();
        currentPlan.setId(2L);
        currentPlan.setPrice(BigDecimal.valueOf(20));
        currentPlan.setBillingCycle(MONTHLY);

        // novo plano (mais barato)
        PlanEntity newPlan = new PlanEntity();
        newPlan.setId(1L);
        newPlan.setPrice(BigDecimal.valueOf(10));
        newPlan.setBillingCycle(MONTHLY);

        ChangeSubscriptionPlanRequest request =
                new ChangeSubscriptionPlanRequest(1L, MONTHLY);

        when(subscriptionRepositoryRead.findById(1L))
                .thenReturn(Optional.of(subscription));

        when(planRepositoryRead.findById(2l))
                .thenReturn(Optional.of(currentPlan));

        when(planRepositoryRead.findById(1l))
                .thenReturn(Optional.of(newPlan));

        when(subscriptionChangeJpaRepositoryWrite.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(subscriptionRepositoryWrite.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        changeSubscriptionPlanService.execute(subscriptionId, request);

        ArgumentCaptor<SubscriptionChangeEntity> changeCaptor = ArgumentCaptor.forClass(SubscriptionChangeEntity.class);
        verify(subscriptionChangeJpaRepositoryWrite).save(changeCaptor.capture());
        assertEquals(2L, changeCaptor.getValue().getOldPlanId());
        assertEquals(1L, changeCaptor.getValue().getNewPlanId());

        ArgumentCaptor<SubscriptionEntity> captor2 = ArgumentCaptor.forClass(SubscriptionEntity.class);
        verify(subscriptionRepositoryWrite).save(captor2.capture());

        SubscriptionEntity entitySalva = captor2.getValue();
        assertEquals(2L, entitySalva.getPlanId());
        assertEquals(1L, entitySalva.getScheduledPlanId());
        assertEquals(BigDecimal.valueOf(20), entitySalva.getCurrentPrice());
        assertEquals(BigDecimal.valueOf(10), entitySalva.getScheduledPrice());

    }

}