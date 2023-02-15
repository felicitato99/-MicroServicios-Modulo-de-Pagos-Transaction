package com.accenture.modulosPago.services;


import com.accenture.modulosPago.dtos.TransactionDto;
import com.accenture.modulosPago.entities.PaymentType;
import com.accenture.modulosPago.entities.Transaction;
import com.accenture.modulosPago.models.Account;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionServiceInter {
    public Transaction createTransaction(TransactionDto transactionDto);

    public Boolean checkAccountNoExist(String accountNumber);

    public List<Account> checkUserNoExist(String userId);

    public Double getBalanceSendingAccount(String accountNumber);

    public String getNameUserAccount(String accountNumber);

    public List<Transaction> findAllTransaction();

    public Transaction findByIdTransaction(Long id);

    public List<Transaction> findByDateBetween(LocalDateTime date1, LocalDateTime date2);

    public List<Transaction> findTransactionsUserByAccountNumber(String accountNumber);

    public List<Transaction> findByPaymentType(PaymentType paymentType);
}
