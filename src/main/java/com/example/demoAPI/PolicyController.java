package com.example.demoAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<String> getPolicyResponse() {
        // Replace with the actual policy microservice URL
        String policyApiUrl = "http://policy-microservice/api/policy";

        // Call the external API
        ResponseEntity<String> response = restTemplate.getForEntity(policyApiUrl, String.class);

        // Return the response from the policy microservice
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}