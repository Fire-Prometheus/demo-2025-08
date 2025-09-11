package com.example.demo.util;

import com.example.demo.domain.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static String createCompositeKey(Transaction transaction) {
        String yearMonth = transaction.getValueDate()
                                      .format(DATE_TIME_FORMATTER);
        return combine(transaction.getIban(), yearMonth);
    }

    public static String createCompositeKey(String iban, int year, int month) {
        var yearMonth = LocalDate.of(year, month, 1)
                                 .format(DATE_TIME_FORMATTER);
        return combine(iban, yearMonth);
    }

    private static String combine(String iban, String yearMonth) {
        return iban + "|" + yearMonth;
    }
}
