package com.example.demoAPI;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DemoApiApplication.class)
public class CircuitBreakerTest {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Test
    public void testCircuitBreakerConfiguration() {
        CircuitBreaker policyBreaker = circuitBreakerRegistry.circuitBreaker("policyService");
        assertThat(policyBreaker).isNotNull();
        assertThat(policyBreaker.getCircuitBreakerConfig().getFailureRateThreshold()).isEqualTo(50.0f);

        CircuitBreaker paymentBreaker = circuitBreakerRegistry.circuitBreaker("paymentService");
        assertThat(paymentBreaker).isNotNull();

        CircuitBreaker customerBreaker = circuitBreakerRegistry.circuitBreaker("customerService");
        assertThat(customerBreaker).isNotNull();
    }
}