package com.example.demo.service;

import com.example.demo.domain.TransactionInfo;
import com.example.demo.domain.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final TransactionMapper transactionMapper;

    public List<String> findByIbanAndYearAndMonth(String iban, LocalDate start, Pageable pageable) {
        var end = start.plusMonths(1)
                       .minusDays(1);

        return repository.findByIbanAndValueDateBetween(iban, start, end, pageable)
                         .map(TransactionInfo::getUuid)
                         .map(UUID::toString)
                         .stream()
                         .toList();
    }
}
