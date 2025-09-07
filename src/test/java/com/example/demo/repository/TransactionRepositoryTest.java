package com.example.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@Sql("/data/transaction-test-data.sql")
@DirtiesContext
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository repository;

    @Test
    void findByIbanAndValueDateBetween_whenDataExists_thenReturnData() {
        // setup
        // execution
        var result = repository.findByIbanAndValueDateBetween("US64SVBKUS6S3300958879", LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31), PageRequest.of(0, 50));
        // verification
        assertEquals(1, result.getNumberOfElements());
        var uuid = result.getContent()
                         .getFirst()
                         .getUuid()
                         .toString();
        assertEquals("550e8400-e29b-41d4-a716-446655440001", uuid);
    }
}