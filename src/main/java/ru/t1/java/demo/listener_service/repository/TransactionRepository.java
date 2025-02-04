package ru.t1.java.demo.listener_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.listener_service.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("FROM Transaction t WHERE t.account = :accountId")
    List<Transaction> findAllAccountId(@Param("accountId") String accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.createdAt >= :startTime")
    List<Transaction> findRecentTransactions(@Param("accountId") String accountId,
                                             @Param("startTime") LocalDateTime startTime);
}
