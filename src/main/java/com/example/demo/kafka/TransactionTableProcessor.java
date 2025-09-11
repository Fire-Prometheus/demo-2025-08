package com.example.demo.kafka;

import com.example.demo.domain.MonthlyTransactions;
import com.example.demo.domain.Transaction;
import com.example.demo.util.TransactionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionTableProcessor {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final String TABLE_NAME = "transactions-by-iban-month";

    public void buildTopology(KStream<String, Transaction> stream) {
        // Group by IBAN + Year-Month composite key
        var materialized = Materialized.<String, MonthlyTransactions, KeyValueStore<Bytes, byte[]>>as(TABLE_NAME)
                                       .withKeySerde(Serdes.String())
                                       .withValueSerde(new JsonSerde<>(MonthlyTransactions.class));
        KTable<String, MonthlyTransactions> groupedTransactions = stream.groupBy((transactionId, transaction) -> TransactionUtil.createCompositeKey(transaction), Grouped.with(Serdes.String(), new JsonSerde<>(Transaction.class)))
                                                                        .aggregate(MonthlyTransactions::new, (compositeKey, transaction, monthlyTransactions) -> this.monthlyTransactionsAggregator(transaction, monthlyTransactions), materialized);
        log.info("Built the KTable for monthly transactions.");
    }

    private MonthlyTransactions monthlyTransactionsAggregator(Transaction transaction, MonthlyTransactions monthlyTransactions) {
        monthlyTransactions.setIban(transaction.getIban());
        monthlyTransactions.setYear(transaction.getValueDate()
                                               .getYear());
        monthlyTransactions.setMonth(monthlyTransactions.getMonth());
        monthlyTransactions.getTransactions()
                           .add(transaction);
        log.trace("Handled the transaction {}", transaction.getUuid());
        return monthlyTransactions;
    }
}
