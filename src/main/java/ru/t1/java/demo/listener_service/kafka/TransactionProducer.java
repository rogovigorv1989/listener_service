package ru.t1.java.demo.listener_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.listener_service.model.dto.TransactionDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import ru.t1.java.demo.listener_service.model.dto.TransactionDTO;

@Slf4j
@Component
public class TransactionProducer<T extends TransactionDTO>{
    @Autowired
    private final KafkaTemplate<String, TransactionDTO> template;

    public TransactionProducer(@Qualifier("transactionKafkaTemplate") KafkaTemplate<String,
            TransactionDTO> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    public void send(TransactionDTO transaction) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), transaction).get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void sendTo(String topic, Object o) {
        try {
            template.send(topic, (TransactionDTO) o).get();
            template.send(topic,
                            1,
                            LocalDateTime.now().toEpochSecond(ZoneOffset.of("+03:00")),
                            UUID.randomUUID().toString(),
                            (TransactionDTO) o)
                    .get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }
}
