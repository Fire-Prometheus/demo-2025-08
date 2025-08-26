package com.example.demo.domain;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class Transaction {
    @NotNull
    UUID uuid;
    @Size(min = 1)
    String iban;
    @NotNull
    LocalDate valueDate;
    String description;
}
