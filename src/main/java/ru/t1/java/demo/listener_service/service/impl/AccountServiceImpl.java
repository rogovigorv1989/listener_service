package ru.t1.java.demo.listener_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.listener_service.model.Account;
import ru.t1.java.demo.listener_service.repository.AccountRepository;
import ru.t1.java.demo.listener_service.service.AccountService;

@Slf4j
@RequiredArgsConstructor
@Service
class AccountServiceImpl implements AccountService {
    @Autowired
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account findById(String id) {
        return accountRepository.findByAccountId(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
}
