package com.example.demo.kafka;

import com.example.demo.domain.Transaction;
import com.example.demo.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionKafkaProducer {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    @Value("#{@transactionTopic.name()}")
    private String topic;
    private final TransactionService transactionService;

    @PostConstruct
    private void initialize() {
        transactionService.findAll()
                          .forEach(this::sendTransaction);
        kafkaTemplate.flush();
        log.info("Sent all transactions to the topic.");
    }

    @SneakyThrows
    public void sendTransaction(Transaction transaction) {
        var uuid = transaction.getUuid()
                              .toString();
        kafkaTemplate.send(topic, uuid, transaction);
        log.debug("Sent the transaction {} out.", uuid);
    }
}
