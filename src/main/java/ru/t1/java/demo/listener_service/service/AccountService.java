package ru.t1.java.demo.listener_service.service;

import ru.t1.java.demo.listener_service.model.Account;

public interface AccountService {
    Account findById(String id);
}
