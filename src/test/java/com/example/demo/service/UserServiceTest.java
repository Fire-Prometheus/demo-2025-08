package com.example.demo.service;

import com.example.demo.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/data/account-test-data.sql")
class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    @DisplayName("When a user has multiple accounts, then return all these accounts.")
    void findIbanByUser_whenUserHasMultipleAccounts_thenReturnMultipleAccounts() {
        // setup
        // execution
        var ibanByUser = service.findIbanByUser("user-001-uuid-12345");
        // verification
        assertEquals(3, ibanByUser.size());
        var actual = ibanByUser.stream()
                               .sorted()
                               .toList();
        var expected = List.of("DE89370400440532013000", "US64SVBKUS6S3300958879", "US64SVBKUS6S3300958881");
        assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("When a user has only 1 account, then return the only 1 account.")
    void findIbanByUser_whenUserHasOnlyOneAccount_thenReturnOnlyOneAccount() {
        // setup
        // execution
        var ibanByUser = service.findIbanByUser("user-006-uuid-44444");
        // verification
        assertEquals(1, ibanByUser.size());
        assertEquals("FR1420041010050500013M02606", ibanByUser.getFirst());
    }
}