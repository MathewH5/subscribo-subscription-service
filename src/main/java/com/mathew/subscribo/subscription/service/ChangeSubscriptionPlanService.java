package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.exception.PlanNotFoundException;
import com.mathew.subscribo.subscription.exception.SubscriptionNotFoundException;
import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.ChangeSubscriptionPlanRequest;
import com.mathew.subscribo.subscription.model.SubscriptionResponse;
import com.mathew.subscribo.subscription.model.enitty.PlanEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionChangeEntity;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionChangeJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryWrite;
import com.mathew.subscribo.subscription.repository.PlanJpaRepositoryRead;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.mathew.subscribo.subscription.model.BillingCycle.MONTHLY;
import static com.mathew.subscribo.subscription.model.BillingCycle.YEARLY;
import static com.mathew.subscribo.subscription.model.ChangeType.DOWNGRADE;

@Service
public class ChangeSubscriptionPlanService {

    //o servico pode ter upgrade ou downgrade
    //tem diversos planos que ele pode mudar
    //como fazer esse gerenciamento

    // preciso de um servico pra mandar pro front qual planos ele pode contratar para poder modficar o dele, tendo validacao e etc

    //o servico vai receber oque?
    //Novo plano -id do plano

    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionJpaRepositoryRead subscriptionRepositoryRead;
    private final SubscriptionJpaRepositoryWrite subscriptionRepositoryWrite;
    private final PlanJpaRepositoryRead planRepositoryRead;
    private final SubscriptionChangeJpaRepositoryWrite subscriptionChangeRepositoryWrite;

    public ChangeSubscriptionPlanService(SubscriptionMapper subscriptionMapper, SubscriptionJpaRepositoryRead subscriptionRepositoryRead, SubscriptionJpaRepositoryWrite subscriptionRepositoryWrite, PlanJpaRepositoryRead planRepositoryRead, SubscriptionChangeJpaRepositoryWrite subscriptionChangeRepositoryWrite) {
        this.subscriptionMapper = subscriptionMapper;
        this.subscriptionRepositoryRead = subscriptionRepositoryRead;
        this.subscriptionRepositoryWrite = subscriptionRepositoryWrite;
        this.planRepositoryRead = planRepositoryRead;
        this.subscriptionChangeRepositoryWrite = subscriptionChangeRepositoryWrite;
    }


    @Transactional
    public SubscriptionResponse execute (Long id, ChangeSubscriptionPlanRequest request){
        SubscriptionEntity entitySubscription = subscriptionRepositoryRead.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));

        PlanEntity entityCurrentPlan = planRepositoryRead.findById(entitySubscription.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException(id));

        PlanEntity entityNewPlan = planRepositoryRead.findById(request.planId())
                .orElseThrow(() -> new PlanNotFoundException(id));


        //como vou realizar a validacao para saber se 'e upgrade ou downgrade? (pois busco no bd de plan oque que tem)
        if (isUpgrade(entityCurrentPlan,entityNewPlan)){
            return upgrade(entitySubscription, entityNewPlan);
        }
        return downgrade(entitySubscription, entityNewPlan, request);
    }

    private boolean isUpgrade(PlanEntity current, PlanEntity next) {
        if (current.getBillingCycle() == MONTHLY && next.getBillingCycle() == YEARLY) {
            return true;
        }

        if (current.getBillingCycle().equals(next.getBillingCycle())) {
            return next.getPrice().compareTo(current.getPrice()) > 0;
        }
        return false;
    }

    private SubscriptionResponse upgrade (SubscriptionEntity entitySubscription, PlanEntity entityNewPlan){
        // atualiza o plano na hora
        // cobra proporcioanl oque foi gasto ate o fim do ciclo
        // o novo sera cobrado por inteiro
        // ja mudo o plano ou mando para algum lugar para rodar no dia do fim do ciclo?

        entitySubscription.setPlanId(entityNewPlan.getId());
        entitySubscription.setBillingCycle(entityNewPlan.getBillingCycle());
        entitySubscription.setCurrentPrice(entityNewPlan.getPrice());

        SubscriptionEntity saved = subscriptionRepositoryWrite.save(entitySubscription);

        return subscriptionMapper.toResponse(saved);

    }

    private SubscriptionResponse downgrade(SubscriptionEntity entitySubscription, PlanEntity entityNewPlan, ChangeSubscriptionPlanRequest request){
        // espera o fim do ciruclo para comecar o novo

        entitySubscription.setScheduledPrice(entityNewPlan.getPrice());
        entitySubscription.setScheduledPlanId(request.planId());
        entitySubscription.setScheduledBillingCycle(request.scheduledBillingCycle());

        SubscriptionEntity saved = subscriptionRepositoryWrite.save(entitySubscription);

        SubscriptionChangeEntity subscriptionChangeEntity = new SubscriptionChangeEntity();
        subscriptionChangeEntity.setChangedAt(entitySubscription.getNextBillingDate());
        subscriptionChangeEntity.setSubscriptionId(entitySubscription.getId());
        subscriptionChangeEntity.setChangeType(DOWNGRADE);
        subscriptionChangeEntity.setOldPlanId(entitySubscription.getPlanId());
        subscriptionChangeEntity.setNewPlanId(entitySubscription.getScheduledPlanId());
        subscriptionChangeEntity.setNewBillingCycle(entityNewPlan.getBillingCycle());
        subscriptionChangeEntity.setNewPrice(entityNewPlan.getPrice());

        subscriptionChangeRepositoryWrite.save(subscriptionChangeEntity);

        return subscriptionMapper.toResponse(saved);
    }

}
