package com.example.demo.kafka;

import com.example.demo.domain.Transaction;
import com.example.demo.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionKafkaProducer {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final NewTopic transactionTopic;
    private final TransactionService transactionService;

    @PostConstruct
    public void initialize() {
        transactionService.findAll()
                          .forEach(this::sendTransaction);
    }

    public void sendTransaction(Transaction transaction) {
        kafkaTemplate.send(transactionTopic.name(), transaction)
                     .whenComplete((result, throwable) -> {
                         var uuid = transaction.getUuid();
                         if (throwable == null) {
                             log.info("Sent the message {}.", uuid);
                         } else {
                             log.warn("Failed to send the message {}.", uuid, throwable);
                         }
                     });
    }
}
