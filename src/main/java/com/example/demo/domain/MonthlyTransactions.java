package com.example.demo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MonthlyTransactions implements Serializable {
    private String iban;
    private int year;
    private int month;
    private List<Transaction> transactions = new ArrayList<>();
}
