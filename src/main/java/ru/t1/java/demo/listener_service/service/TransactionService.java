package ru.t1.java.demo.listener_service.service;

import ru.t1.java.demo.listener_service.model.Transaction;

public interface TransactionService {
    void processTransaction(Transaction transaction);
}
