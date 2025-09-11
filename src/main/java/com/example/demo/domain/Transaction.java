package com.example.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {
    @NotNull
    @EqualsAndHashCode.Include
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
