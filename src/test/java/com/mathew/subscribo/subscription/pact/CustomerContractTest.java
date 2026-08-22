package com.mathew.subscribo.subscription.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;

import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;

import au.com.dius.pact.core.model.annotations.Pact;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "customer-service")
class CustomerContractTest {

    @Pact(provider = "customer-service", consumer = "subscription-service")
    public RequestResponsePact getCustomerPact(PactDslWithProvider builder) {

        return builder
                .given("customer with id 1 exists")
                .uponReceiving("a request for customer 1")
                .path("/customer/1")
                .method("GET")

                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .integerType("id", 1)
                        .stringType("name", "Mathew")
                        .stringType("email", "mathewhana@email.com")
                        .stringType("phoneNumber", "31999998885")
                        .stringType("birthDate", "2010-11-10")
                        .stringType("cpf", "12345678909")
                        .stringType("createdAt", "2026-01-23T21:31:47.74288")
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getCustomerPact")
    void testGetCustomer(MockServer mockServer) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(mockServer.getUrl() + "/customer/1"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
}