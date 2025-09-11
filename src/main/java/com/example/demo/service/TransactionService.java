package com.example.demo.service;

import com.example.demo.domain.MonthlyTransactions;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.util.TransactionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.demo.kafka.TransactionTableProcessor.TABLE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private final TransactionRepository repository;
    private final TransactionMapper transactionMapper;
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Retryable({InvalidStateStoreException.class})
    public MonthlyTransactions getTransactionsByIbanAndYearAndMonth(String iban, int year, int month) {
        var streams = streamsBuilderFactoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, MonthlyTransactions> store = streams.store(StoreQueryParameters.fromNameAndType(TABLE_NAME, QueryableStoreTypes.keyValueStore()));
        String compositeKey = TransactionUtil.createCompositeKey(iban, year, month);
        var monthlyTransactions = store.get(compositeKey);
        var size = monthlyTransactions.getTransactions()
                                      .size();
        log.info("Found {} result(s) for account {} in {}{}", size, iban, year, month);
        return monthlyTransactions;
    }

    public List<Transaction> findAll() {
        return repository.findAll()
                         .stream()
                         .map(transactionMapper::toDomain)
                         .toList();
    }
}
