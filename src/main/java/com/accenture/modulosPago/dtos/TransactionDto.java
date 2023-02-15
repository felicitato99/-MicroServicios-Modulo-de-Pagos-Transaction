package com.accenture.modulosPago.dtos;

import com.accenture.modulosPago.entities.PaymentType;
import com.accenture.modulosPago.entities.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TransactionDto {
    private Double amount;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime transactionDate;
    private String beneficiary;

    private TransactionType transactionType;
    private PaymentType paymentType;
    private String sendingAccount;
    private  String receiverAccount;

    private String description;


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
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

}
