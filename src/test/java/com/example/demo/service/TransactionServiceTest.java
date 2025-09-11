package com.example.demo.service;

import com.example.demo.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext
@Sql({"/data/account-test-data.sql", "/data/transaction-test-data.sql"})
@ActiveProfiles("test")
class TransactionServiceTest {
    @Autowired
    private TransactionService service;

    @Test
    void getTransactionsByIbanAndYearAndMonth_whenIbanAndYearAndMonthMatch_thenReturnTransactions() {
        // setup
        // execution
        var monthlyTransactions = service.getTransactionsByIbanAndYearAndMonth("US64SVBKUS6S3300958881", 2025, 8);
        // verification
        var transactions = monthlyTransactions.getTransactions();
        assertEquals(1, transactions.size());
        var transaction = transactions.iterator()
                                      .next();
        assertEquals("US64SVBKUS6S3300958881", transaction.getIban());
        assertEquals("USD", transaction.getCurrency());
        assertEquals("-199.990000", transaction.getAmount()
                                               .toPlainString());
        assertEquals(LocalDate.parse("2025-08-19"), transaction.getValueDate());
        assertEquals("Online subscription renewal", transaction.getDescription());

    }
}