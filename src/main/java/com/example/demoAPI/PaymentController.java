package com.example.demoAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<String> getPaymentResponse() {
        // Replace with the actual payment microservice URL
        String paymentApiUrl = "http://payment-microservice/api/payment";

        // Call the external API
        ResponseEntity<String> response = restTemplate.getForEntity(paymentApiUrl, String.class);

        // Return the response from the payment microservice
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}