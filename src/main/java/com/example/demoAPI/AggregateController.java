package com.example.demoAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
public class AggregateController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/aggregate")
    public ResponseEntity<Map<String, String>> getAggregateResponse() {
        // Define the URLs for the microservices
        String policyUrl = "http://policy-microservice/api/policy";
        String paymentUrl = "http://payment-microservice/api/payment";
        String customerUrl = "http://customer-microservice/api/customer";

        // Create CompletableFutures for parallel calls with timeout
        CompletableFuture<String> policyFuture = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(policyUrl, String.class);
                return response.getBody();
            } catch (Exception e) {
                throw new RuntimeException("Failed to call policy service", e);
            }
        }).orTimeout(5, TimeUnit.SECONDS);

        CompletableFuture<String> paymentFuture = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(paymentUrl, String.class);
                return response.getBody();
            } catch (Exception e) {
                throw new RuntimeException("Failed to call payment service", e);
            }
        }).orTimeout(5, TimeUnit.SECONDS);

        CompletableFuture<String> customerFuture = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(customerUrl, String.class);
                return response.getBody();
            } catch (Exception e) {
                throw new RuntimeException("Failed to call customer service", e);
            }
        }).orTimeout(5, TimeUnit.SECONDS);

        // Combine all futures
        CompletableFuture<Void> allOf = CompletableFuture.allOf(policyFuture, paymentFuture, customerFuture);

        try {
            // Wait for all to complete with a timeout of 10 seconds
            allOf.get(10, TimeUnit.SECONDS);

            // Get the results
            String policyResponse = policyFuture.get();
            String paymentResponse = paymentFuture.get();
            String customerResponse = customerFuture.get();

            // Prepare the response map
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("policy", policyResponse);
            responseMap.put("payment", paymentResponse);
            responseMap.put("customer", customerResponse);

            return ResponseEntity.ok(responseMap);
        } catch (TimeoutException e) {
            return ResponseEntity.status(504).body(Map.of("error", "Request timed out"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).body(Map.of("error", "One or more services failed: " + e.getMessage()));
        }
    }
}