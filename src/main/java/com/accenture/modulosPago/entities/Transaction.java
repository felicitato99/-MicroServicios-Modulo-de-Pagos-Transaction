package com.accenture.modulosPago.entities;

import com.accenture.modulosPago.dtos.TransactionDto;
import com.accenture.modulosPago.models.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native",strategy = "native")
    private Long id;
    private Double amount;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime transactionDate;
    private String beneficiary;
    private TransactionType transactionType;
    private PaymentType paymentType;

    private String sendingAccount;
    private String receiverAccount;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTransaction() {
        return transactionDate;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.transactionDate = dateTransaction;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getSendingAccount() {
        return sendingAccount;
    }

    public void setSendingAccount(String sendingAccount) {
        this.sendingAccount = sendingAccount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction() {
    }

    public Transaction(TransactionDto transactionDTO){
        this.amount = transactionDTO.getAmount();
        this.transactionDate = transactionDTO.getTransactionDate();
        this.beneficiary = transactionDTO.getBeneficiary();
        this.transactionType = transactionDTO.getTransactionType();
        this.paymentType = transactionDTO.getPaymentType();
        this.sendingAccount = transactionDTO.getSendingAccount();
        this.receiverAccount = transactionDTO.getReceiverAccount();
        this.description = transactionDTO.getDescription();
    }

}
