package com.example.demo.service;

import com.example.demo.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext
@ActiveProfiles("test")
class RateServiceTest {
    @Autowired
    private RateService rateService;

    @Test
    @DisplayName("When base currency and quote currency match exactly, then return the exact rate.")
    void getRate_whenBaseCurrencyAndRateCurrencyMatchExactly_thenReturnExactRate() {
        // setup
        // execution
        var rate = rateService.getRate("USD", "HKD");
        // verification
        assertEquals("7.7666", rate.toPlainString());
    }

    @Test
    @DisplayName("When base currency and quote currency match inversely, then return the inverted rate.")
    void getRate_whenBaseCurrencyAndRateCurrencyMatchInversely_thenReturnInvertedRate() {
        // setup
        // execution
        var rate = rateService.getRate("HKD", "USD");
        // verification
        assertEquals("0.12876", rate.toPlainString());
    }
}