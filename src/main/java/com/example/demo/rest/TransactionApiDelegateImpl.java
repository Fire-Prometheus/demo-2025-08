package com.example.demo.rest;

import com.example.demo.api.TransactionApiDelegate;
import com.example.demo.api.model.TransactionDto;
import com.example.demo.api.model.TransactionSummaryDto;
import com.example.demo.util.JwtUtil;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.mapper.TransactionMapper;
import com.example.demo.service.RateService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionApiDelegateImpl implements TransactionApiDelegate {
    private final TransactionService service;
    private final RateService rateService;
    private final UserService userService;
    private final TransactionMapper mapper;
    private final Comparator<Transaction> comparator = Comparator.comparing(Transaction::getValueDate)
                                                                 .thenComparing(Transaction::getCurrency)
                                                                 .thenComparing(Transaction::getAmount);

    @Override
    public ResponseEntity<TransactionSummaryDto> findTransactionsByUserAndYearAndMonthAndEquivalentCurrency(LocalDate yearMonth, String equivalentCurrency, Integer page, Integer pageSize) {
        if (page < 1) {
            throw new IllegalArgumentException("Page should start from 1.");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size should start from 1.");
        }
        var skip = (page - 1) * pageSize;
        // get user
        var userId = JwtUtil.getUserId();
        // get the related accounts
        var ibanList = userService.findIbanByUser(userId);
        // get the transactions
        var transactions = ibanList.stream()
                                   .map(iban -> service.getTransactionsByIbanAndYearAndMonth(iban, yearMonth.getYear(), yearMonth.getMonthValue()))
                                   .flatMap(monthlyTransactions -> monthlyTransactions.getTransactions()
                                                                                      .stream())
                                   .toList();
        var total = transactions.size();
        if (skip >= total) {
            throw new IllegalArgumentException("Page exceeds the max. supported number.");
        }
        var currentPage = transactions.stream()
                                      .sorted(comparator)
                                      .skip(skip)
                                      .limit(pageSize)
                                      .map(transaction -> completeDto(transaction, equivalentCurrency))
                                      .toList();
        var transactionSummaryDto = new TransactionSummaryDto();
        transactionSummaryDto.setPage(page);
        transactionSummaryDto.setPageSize(pageSize);
        transactionSummaryDto.setTotal(total);
        transactionSummaryDto.setTransactions(currentPage);
        return ResponseEntity.ok(transactionSummaryDto);
    }

    private TransactionDto completeDto(Transaction transaction, String quoteCurrency) {
        var dto = mapper.toDto(transaction);
        dto.setEquivalentCurrency(quoteCurrency);
        var rate = rateService.getRate(transaction.getCurrency(), quoteCurrency);
        dto.setEquivalentAmount(transaction.getAmount()
                                           .multiply(rate)
                                           .doubleValue());
        return dto;
    }
}
