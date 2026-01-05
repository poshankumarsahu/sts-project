package com.example.demoAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DemoApiApplication.class)
public class CacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCacheConfiguration() {
        Cache policyCache = cacheManager.getCache("policyCache");
        assertThat(policyCache).isNotNull();

        Cache paymentCache = cacheManager.getCache("paymentCache");
        assertThat(paymentCache).isNotNull();

        Cache customerCache = cacheManager.getCache("customerCache");
        assertThat(customerCache).isNotNull();
    }
}