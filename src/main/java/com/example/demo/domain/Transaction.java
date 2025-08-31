package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Transaction {
    @NotNull
    UUID uuid;
    @Size(min = 1)
    String iban;
    @Size(min = 3, max = 3)
    String currency;
    @NotNull
    BigDecimal amount;
    @NotNull
    LocalDate valueDate;
    String description;
}
