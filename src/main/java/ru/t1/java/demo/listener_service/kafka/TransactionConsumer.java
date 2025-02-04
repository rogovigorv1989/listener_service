package ru.t1.java.demo.listener_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.listener_service.mapper.TransactionMapper;
import ru.t1.java.demo.listener_service.model.Transaction;
import ru.t1.java.demo.listener_service.model.dto.TransactionDTO;
import ru.t1.java.demo.listener_service.service.TransactionService;


import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionConsumer {
    @Qualifier("transactionMapperImpl")
    @Autowired
    private final TransactionMapper mapper;

    @Autowired
    private final TransactionService transactionService;

    @KafkaListener(groupId = "${t1.kafka.consumer.group-id}",
            topics = {"${t1.kafka.topic.t1_demo_transaction_accept}"},
            containerFactory = "kafkaTransactionListenerContainerFactory")
    public void listener(@Payload List<TransactionDTO> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("listener_service transaction consumer: Обработка новых сообщений");

        try {
            log.error("Topic: " + topic);
            log.error("Key: " + key);
            messageList.stream()
                    .forEach(System.err::println);
            List<Transaction> transactions = messageList.stream()
                    .map(dto -> {
                        dto.setAccountId(dto.getAccountId());
                        dto.setTransactionAmount(dto.getTransactionAmount());
                        dto.setTransactionTime(dto.getTransactionTime());
                        dto.setIsDeleted(dto.getIsDeleted());
                        dto.setClientId(dto.getClientId());
                        dto.setTransactionId(dto.getTransactionId());
                        dto.setCreatedAt(dto.getCreatedAt());
                        dto.setAccountBalance(dto.getAccountBalance());
                        return mapper.toEntity(dto);
                    }).toList();
            transactions.forEach(transactionService::processTransaction);
            log.debug("listener_service transaction consumer: записи обработаны");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            ack.acknowledge();
        }
    }
}
