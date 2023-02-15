package com.accenture.modulosPago.repositories;

import com.accenture.modulosPago.entities.PaymentType;
import com.accenture.modulosPago.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(LocalDateTime date1, LocalDateTime date2);
    List<Transaction>findBySendingAccountOrReceiverAccount(String accountNumber1, String accountNumber2);

    List<Transaction>findByPaymentType(PaymentType paymentType);
}
