package com.example.demoAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<String> getCustomerResponse() {
        // Replace with the actual customer microservice URL
        String customerApiUrl = "http://customer-microservice/api/customer";

        // Call the external API
        ResponseEntity<String> response = restTemplate.getForEntity(customerApiUrl, String.class);

        // Return the response from the customer microservice
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}