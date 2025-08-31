package com.example.demo.kafka;

import com.example.demo.domain.MonthlyTransactions;
import com.example.demo.domain.Transaction;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionStreamProcessor {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private final StreamsConfig streamsConfig;
    private final NewTopic transactionTopic;
    private KafkaStreams streams;

    @PostConstruct
    public void startStreams() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "transaction-grouping-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()
                                                                      .getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);

        StreamsBuilder builder = new StreamsBuilder();
        buildTopology(builder);

        streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    @PreDestroy
    public void closeStreams() {
        if (streams != null) {
            streams.close();
        }
    }

    private void buildTopology(StreamsBuilder builder) {
        // Read transaction stream
        KStream<String, Transaction> transactionStream = builder.stream(transactionTopic.name(), Consumed.with(Serdes.String(), new JsonSerde<>(Transaction.class)));
        // Group by IBAN + Year-Month composite key
        var materialized = Materialized.<String, MonthlyTransactions, KeyValueStore<Bytes, byte[]>>as("transactions-by-iban-month")
                                       .withKeySerde(Serdes.String())
                                       .withValueSerde(new JsonSerde<>(MonthlyTransactions.class));
        KTable<String, MonthlyTransactions> groupedTransactions = transactionStream.groupBy((transactionId, transaction) -> createCompositeKey(transaction), Grouped.with(Serdes.String(), new JsonSerde<>(Transaction.class)))
                                                                                   .aggregate(MonthlyTransactions::new, (compositeKey, transaction, monthlyTransactions) -> this.monthlyTransactionsAggregator(transaction, monthlyTransactions), materialized);
    }

    private String createCompositeKey(Transaction transaction) {
        String yearMonth = transaction.getValueDate()
                                      .format(DATE_TIME_FORMATTER);
        return transaction.getIban() + "|" + yearMonth;
    }

    private MonthlyTransactions monthlyTransactionsAggregator(Transaction transaction, MonthlyTransactions monthlyTransactions) {
        monthlyTransactions.setIban(transaction.getIban());
        monthlyTransactions.setYear(transaction.getValueDate()
                                               .getYear());
        monthlyTransactions.setMonth(monthlyTransactions.getMonth());
        monthlyTransactions.getTransactions()
                           .add(transaction);
        return monthlyTransactions;
    }
}
