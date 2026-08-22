package com.mathew.subscribo.subscription.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "clients.customer")
public record CustomerClientProperties(
        URI baseUrl
) {
}
