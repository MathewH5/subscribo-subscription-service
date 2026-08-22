package com.mathew.subscribo.subscription.client;

import com.mathew.subscribo.subscription.config.CustomerClientProperties;
import com.mathew.subscribo.subscription.exception.CustomerNotFoundException;
import com.mathew.subscribo.subscription.exception.CustomerServiceUnavailableException;
import com.mathew.subscribo.subscription.model.CustomerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Component
public class CustomerWebClient implements CustomerClient{

    private final WebClient webClient;

    public CustomerWebClient(WebClient.Builder webClientBuilder, CustomerClientProperties customerClient) {
        this.webClient = webClientBuilder
                .baseUrl(customerClient.baseUrl().toString())
                .build();
    }

    @Override
    public CustomerResponse getCustomer(Long customerId) {
        return webClient
                .get()
                .uri("/customer/{id}", customerId)
                .header("X-Service-Caller", "subscription-service")
                .retrieve()

                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(
                                new CustomerNotFoundException(customerId)
                        )
                )

                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response
                                .createException()
                                .map(exception ->
                                        new CustomerServiceUnavailableException(
                                                "Customer Service returned an internal error",
                                                exception
                                        )
                                )
                )

                .bodyToMono(CustomerResponse.class)
                .onErrorMap(
                        WebClientRequestException.class,
                        exception -> new CustomerServiceUnavailableException(
                                "Could not connect to Customer Service",
                                exception
                        )
                )
                .block();
    }
}
