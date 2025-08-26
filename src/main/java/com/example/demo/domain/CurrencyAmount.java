package com.example.demo.domain;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Value
public class CurrencyAmount {
    @Size(min = 3, max = 3)
    String currency;
    @NotNull
    BigDecimal amount;
}
