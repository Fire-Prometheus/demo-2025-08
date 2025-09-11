package com.example.demo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MonthlyTransactions implements Serializable {
    private String iban;
    private int year;
    private int month;
    private Set<Transaction> transactions = new HashSet<>();
}
