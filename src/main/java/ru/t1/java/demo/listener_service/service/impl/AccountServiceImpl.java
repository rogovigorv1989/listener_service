package ru.t1.java.demo.listener_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.listener_service.model.Account;
import ru.t1.java.demo.listener_service.repository.AccountRepository;
import ru.t1.java.demo.listener_service.service.AccountService;

@Slf4j
@Service
class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Account findById(String id) {
        return accountRepository.findByAccountId(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
}
