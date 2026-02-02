package com.mathew.subscribo.subscription.client;

import com.mathew.subscribo.subscription.model.CustomerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CustomerWebClient implements CustomerClient{

    private final WebClient webClient;

    public CustomerWebClient(WebClient.Builder webClientBuilder ) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Override
    public CustomerResponse getCustomer(Long customerId) {
        return webClient
                .get()
                .uri("/customer/{id}",customerId)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .block();
    }
}
