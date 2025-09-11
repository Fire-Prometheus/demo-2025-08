package com.example.demo.rest;

import com.example.demo.TestcontainersConfiguration;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.Base64;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@DirtiesContext
@ActiveProfiles("test")
@Sql(value = {"/data/account-test-data.sql", "/data/transaction-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class TransactionApiDelegateImplIT {
    @Autowired
    private MockMvc mockMvc;
    @Value("${secrets.jwt}")
    private String jwtSecret;

    @Test
    @DisplayName("When the given page size is invalid, then return an error.")
    void findTransactionsByUserAndYearAndMonthAndEquivalentCurrency_whenPageSizeIsInvalid_thenReturnError() throws Exception {
        // setup
        var yearMonth = LocalDate.of(2025, 8, 1)
                                 .toString();
        var equivalentCurrency = "HKD";
        var page = 1;
        var pageSize = 0;
        // execution
        // verification
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/transaction")
                                                   .header("Authorization", "Bearer " + getJwt())
                                                   .queryParam("year-month", yearMonth)
                                                   .queryParam("equivalentCurrency", equivalentCurrency)
                                                   .queryParam("page", String.valueOf(page))
                                                   .queryParam("pageSize", String.valueOf(pageSize));
        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.description").value("Page size should start from 1."));
    }

    @Test
    @DisplayName("When the given page does not start from 1, then return an error.")
    void findTransactionsByUserAndYearAndMonthAndEquivalentCurrency_whenPageNumberDoesNotStartFrom1_thenReturnError() throws Exception {
        // setup
        var yearMonth = LocalDate.of(2025, 8, 1)
                                 .toString();
        var equivalentCurrency = "HKD";
        var page = 0;
        var pageSize = 10;
        // execution
        // verification
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/transaction")
                                                   .header("Authorization", "Bearer " + getJwt())
                                                   .queryParam("year-month", yearMonth)
                                                   .queryParam("equivalentCurrency", equivalentCurrency)
                                                   .queryParam("page", String.valueOf(page))
                                                   .queryParam("pageSize", String.valueOf(pageSize));
        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.description").value("Page should start from 1."));
    }

    @Test
    @DisplayName("When the given page exceeds the maximum result, then return an error.")
    void findTransactionsByUserAndYearAndMonthAndEquivalentCurrency_whenPageExceedsTotalNumber_thenReturnError() throws Exception {
        // setup
        var yearMonth = LocalDate.of(2025, 8, 1)
                                 .toString();
        var equivalentCurrency = "HKD";
        var page = 10;
        var pageSize = 10;
        // execution
        // verification
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/transaction")
                                                   .header("Authorization", "Bearer " + getJwt())
                                                   .queryParam("year-month", yearMonth)
                                                   .queryParam("equivalentCurrency", equivalentCurrency)
                                                   .queryParam("page", String.valueOf(page))
                                                   .queryParam("pageSize", String.valueOf(pageSize));
        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.description").value("Page exceeds the max. supported number."));
    }

    @Test
    @DisplayName("When the parameters are valid and requesting for the first page, then return the first page.")
    void findTransactionsByUserAndYearAndMonthAndEquivalentCurrency_whenParametersAreValidAndRequestingFirstPage_thenReturnFirstPage() throws Exception {
        // setup
        var yearMonth = LocalDate.of(2025, 8, 1)
                                 .toString();
        var equivalentCurrency = "HKD";
        var page = 1;
        var pageSize = 2;
        // execution
        // verification
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/transaction")
                                                   .header("Authorization", "Bearer " + getJwt())
                                                   .queryParam("year-month", yearMonth)
                                                   .queryParam("equivalentCurrency", equivalentCurrency)
                                                   .queryParam("page", String.valueOf(page))
                                                   .queryParam("pageSize", String.valueOf(pageSize));
        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(page))
               .andExpect(jsonPath("$.pageSize").value(pageSize))
               .andExpect(jsonPath("$.total").value(3))
               .andExpect(jsonPath("$.transactions", hasSize(2)))
               // first entry
               .andExpect(jsonPath("$.transactions[0].uuid").value("550e8400-e29b-41d4-a716-446655440010"))
               .andExpect(jsonPath("$.transactions[0].iban").value("US64SVBKUS6S3300958881"))
               .andExpect(jsonPath("$.transactions[0].currency").value("USD"))
               .andExpect(jsonPath("$.transactions[0].amount").value(-199.990000))
               .andExpect(jsonPath("$.transactions[0].equivalentCurrency").value("HKD"))
               .andExpect(jsonPath("$.transactions[0].equivalentAmount").value(-1553.242334))
               .andExpect(jsonPath("$.transactions[0].valueDate").value("2025-08-19"))
               .andExpect(jsonPath("$.transactions[0].description").value("Online subscription renewal"))
               // second entry
               .andExpect(jsonPath("$.transactions[1].uuid").value("550e8400-e29b-41d4-a716-446655440002"))
               .andExpect(jsonPath("$.transactions[1].iban").value("DE89370400440532013000"))
               .andExpect(jsonPath("$.transactions[1].currency").value("EUR"))
               .andExpect(jsonPath("$.transactions[1].amount").value(-89.990000))
               .andExpect(jsonPath("$.transactions[1].equivalentCurrency").value("HKD"))
               .andExpect(jsonPath("$.transactions[1].equivalentAmount").value(-805.356506))
               .andExpect(jsonPath("$.transactions[1].valueDate").value("2025-08-27"))
               .andExpect(jsonPath("$.transactions[1].description").value("Online purchase - Electronics"));
    }

    @Test
    @DisplayName("When the parameters are valid and requesting for the second page, then return the second page.")
    void findTransactionsByUserAndYearAndMonthAndEquivalentCurrency_whenParametersAreValidAndRequestingSecondPage_thenReturnSecondPage() throws Exception {
        // setup
        var yearMonth = LocalDate.of(2025, 8, 1)
                                 .toString();
        var equivalentCurrency = "HKD";
        var page = 2;
        var pageSize = 2;
        // execution
        // verification
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/transaction")
                                                   .header("Authorization", "Bearer " + getJwt())
                                                   .queryParam("year-month", yearMonth)
                                                   .queryParam("equivalentCurrency", equivalentCurrency)
                                                   .queryParam("page", String.valueOf(page))
                                                   .queryParam("pageSize", String.valueOf(pageSize));
        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.page").value(page))
               .andExpect(jsonPath("$.pageSize").value(pageSize))
               .andExpect(jsonPath("$.total").value(3))
               .andExpect(jsonPath("$.transactions", hasSize(1)))
               // first entry
               .andExpect(jsonPath("$.transactions[0].uuid").value("550e8400-e29b-41d4-a716-446655440001"))
               .andExpect(jsonPath("$.transactions[0].iban").value("US64SVBKUS6S3300958879"))
               .andExpect(jsonPath("$.transactions[0].currency").value("USD"))
               .andExpect(jsonPath("$.transactions[0].amount").value(1250.750000))
               .andExpect(jsonPath("$.transactions[0].equivalentCurrency").value("HKD"))
               .andExpect(jsonPath("$.transactions[0].equivalentAmount").value(9714.07495))
               .andExpect(jsonPath("$.transactions[0].valueDate").value("2025-08-28"))
               .andExpect(jsonPath("$.transactions[0].description").value("Salary payment August 2025"));
    }

    private String getJwt() {
        return Jwts.builder()
                   .id("user-001-uuid-12345")
                   .signWith(new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256"))
                   .compact();
    }
}