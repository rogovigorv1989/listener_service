package ru.t1.java.demo.listener_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.listener_service.kafka.TransactionProducer;
import ru.t1.java.demo.listener_service.mapper.TransactionMapper;
import ru.t1.java.demo.listener_service.model.Account;
import ru.t1.java.demo.listener_service.model.Transaction;
import ru.t1.java.demo.listener_service.model.dto.TransactionDTO;
import ru.t1.java.demo.listener_service.repository.AccountRepository;
import ru.t1.java.demo.listener_service.repository.TransactionRepository;
import ru.t1.java.demo.listener_service.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.t1.java.demo.listener_service.config.Сonstants.TRANSACTIONS_LIMIT;
import static ru.t1.java.demo.listener_service.config.Сonstants.TRANSACTIONS_PERIOD;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Value("${t1.kafka.topic.t1_demo_transaction_result}")
    private String transactionResultTopic;

    private final TransactionMapper mapper;
    private final TransactionProducer<TransactionDTO> transactionProducer;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(@Qualifier("transactionMapperImpl") TransactionMapper mapper,
                                  TransactionProducer<TransactionDTO> transactionProducer,
                                  TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.mapper = mapper;
        this.transactionProducer = transactionProducer;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void processTransaction(Transaction transaction) {

        List<Transaction> recentTransactions = transactionRepository.findRecentTransactions(
                transaction.getAccount().getAccountId(), LocalDateTime.now().minusSeconds(TRANSACTIONS_PERIOD));

        if (recentTransactions.size() >= TRANSACTIONS_LIMIT) {
            recentTransactions.forEach(t -> t.setStatus(Transaction.Status.BLOCKED));
            transactionProducer.sendTo(transactionResultTopic, transaction);
            return;
        }

        Optional<Account> account =
                Optional.ofNullable(accountRepository.findByAccountId(transaction.getAccount().getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found")));

        transaction.setStatus(account.filter(acc -> transaction.getTransactionAmount() <= acc.getBalance())
                .map(acc -> Transaction.Status.ACCEPTED).orElse(Transaction.Status.REJECTED));
        transactionProducer.sendTo(transactionResultTopic, mapper.toDto(transaction));
    }
}
