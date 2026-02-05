package com.mathew.subscribo.subscription.service;

import com.mathew.subscribo.subscription.mapper.SubscriptionMapper;
import com.mathew.subscribo.subscription.model.SubscriptionResponse;
import com.mathew.subscribo.subscription.model.enitty.SubscriptionEntity;
import com.mathew.subscribo.subscription.repository.SubscriptionJpaRepositoryRead;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetSubscriptionService {

    private final SubscriptionMapper mapper;
    private final SubscriptionJpaRepositoryRead repositoryRead;

    public GetSubscriptionService(SubscriptionMapper mapper, SubscriptionJpaRepositoryRead repositoryRead) {
        this.mapper = mapper;
        this.repositoryRead = repositoryRead;
    }

    public List<SubscriptionResponse> execute (){
        return repositoryRead.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private SubscriptionResponse toDTO(SubscriptionEntity entity){
        return mapper.toResponse(entity);
    }

}
