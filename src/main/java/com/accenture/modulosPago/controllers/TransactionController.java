package com.accenture.modulosPago.controllers;
import com.accenture.modulosPago.Utils;
import com.accenture.modulosPago.entities.Transaction;
import com.accenture.modulosPago.models.Account;
import com.accenture.modulosPago.dtos.TransactionDto;
import com.accenture.modulosPago.entities.PaymentType;
import com.accenture.modulosPago.services.TransactionServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionServiceInter transactionServiceInter;

    @Transactional
    @PostMapping("/generateTransactions")
    public ResponseEntity<Object> generateTransactions(@RequestBody TransactionDto transactionDto) {
        try {
            if (transactionServiceInter.checkAccountNoExist(transactionDto.getSendingAccount())) {
                return new ResponseEntity<>("Account Sender NO exist, check data", HttpStatus.FORBIDDEN);
            }
            if (transactionServiceInter.checkAccountNoExist(transactionDto.getReceiverAccount())) {
                return new ResponseEntity<>("Account Receiver NO exist, check data", HttpStatus.FORBIDDEN);
            }
            if (transactionDto.getAmount().toString().trim().isEmpty() || transactionDto.getDescription().trim().isEmpty() || transactionDto.getSendingAccount().trim().isEmpty() || transactionDto.getReceiverAccount().trim().isEmpty()) {
                return new ResponseEntity<>("Error Missing data, description or account sender or account destination is empty or amount", HttpStatus.NOT_ACCEPTABLE);
            }
            if (transactionDto.getSendingAccount().equals(transactionDto.getReceiverAccount())) {
                return new ResponseEntity<>("Error the number account of Sender and Receiver MUST NOT BE the same", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!(transactionDto.getSendingAccount().length() == 10)) {
                return new ResponseEntity<>("Error check Account Number of Sender, it will be 10 digits", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!Utils.verifyNumber(transactionDto.getSendingAccount())) {
                return new ResponseEntity<>("Error in Account Sender, please check it only numbers.", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!(transactionDto.getReceiverAccount().length() == 10)) {
                return new ResponseEntity<>("Error check Account Number of Receiver, it will be 10 digits", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!Utils.verifyNumber(transactionDto.getReceiverAccount())) {
                return new ResponseEntity<>("Error in Account Receiver, please check it only numbers.", HttpStatus.NOT_ACCEPTABLE);
            }
            if (transactionDto.getAmount() < 0.00) {
                return new ResponseEntity<>("Error Amount must to be major 0.", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!Utils.verifyNumber(transactionDto.getAmount().toString())) {
                return new ResponseEntity<>("Error in Amount, please check it only numbers.", HttpStatus.NOT_ACCEPTABLE);
            }
            if (Utils.verifyTwoDecimal(transactionDto.getAmount())) {
                return new ResponseEntity<>("Error in Amount, must be only two decimal, please check it only numbers.", HttpStatus.NOT_ACCEPTABLE);
            }
            if (transactionServiceInter.getBalanceSendingAccount(transactionDto.getSendingAccount()) < transactionDto.getAmount()) {
                return new ResponseEntity<>("Error The Account do not have balance", HttpStatus.NOT_ACCEPTABLE);
            }
            if (transactionDto.getTransactionDate().toLocalDate().isBefore(LocalDate.now())) {
                return new ResponseEntity<>("Error The Date must be now or bigger", HttpStatus.NOT_ACCEPTABLE);
            }
            if (!Utils.checkFormatDate(transactionDto.getTransactionDate().toLocalDate())) {
                return new ResponseEntity<>("Error with format date, remember DD/HH/YYYY", HttpStatus.NOT_ACCEPTABLE);
            }
            transactionDto.setBeneficiary(transactionServiceInter.getNameUserAccount(transactionDto.getReceiverAccount()));
            Transaction transaction =transactionServiceInter.createTransaction(transactionDto);
            return new ResponseEntity<>(transaction,HttpStatus.CREATED);

        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
            return new ResponseEntity<>("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listAll")
    public List<com.accenture.modulosPago.entities.Transaction> getListAllTransaction() {
        return transactionServiceInter.findAllTransaction();
    }

    @GetMapping("/listByUser/{idUser}")
    public ResponseEntity<Object> getListByUser(@PathVariable Long idUser) {
        List<Account> accountList = transactionServiceInter.checkUserNoExist(idUser.toString());
        if (accountList == null) {
            return new ResponseEntity<>("Error User NO exits", HttpStatus.NOT_ACCEPTABLE);
        }
        if (accountList.isEmpty()) {
            return new ResponseEntity<>("Error User NO have account", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Transaction> transactionListFull = new ArrayList<>();
        for (Account accountAux : accountList) {
            List<Transaction> transactionList= transactionServiceInter.findTransactionsUserByAccountNumber(accountAux.getAccountNumber());
            transactionListFull.addAll(transactionList);
        }
        return new ResponseEntity<>(transactionListFull, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Object> detailUser(@PathVariable Long id) {
        Transaction transaction = transactionServiceInter.findByIdTransaction(id);
        if (transaction == null) {
            return new ResponseEntity<>("Transaction NO exits, check information", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/BetweenDates")
    public ResponseEntity<Object> getTransactionBetweenDates(@RequestParam String date1, @RequestParam String date2) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        if (!Utils.checkFormatDate(LocalDate.parse(date1, formatter1))) {
            return new ResponseEntity<>("Error with format date1, remember DD/MM/YYYY", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Utils.checkFormatDate(LocalDate.parse(date2, formatter1))) {
            return new ResponseEntity<>("Error with format date2, remember DD/MM/YYYY", HttpStatus.NOT_ACCEPTABLE);
        }
        date1 = date1 + " 00:00:00";
        date2 = date2 + " 23:59:59";
        List<Transaction> transactionList = transactionServiceInter.findByDateBetween(LocalDateTime.parse(date1, formatter2), LocalDateTime.parse(date2, formatter2));
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @GetMapping("/findByDate")
    public ResponseEntity<Object> getTransactionFindByDate(@RequestParam String date) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        if (!Utils.checkFormatDate(LocalDate.parse(date, formatter1))) {
            return new ResponseEntity<>("Error with format date1, remember DD/HH/YYYY", HttpStatus.NOT_ACCEPTABLE);
        }
        String date1 = date + " 00:00:00";
        String date2 = date + " 23:59:59";
        List<Transaction> transactionList = transactionServiceInter.findByDateBetween(LocalDateTime.parse(date1, formatter2), LocalDateTime.parse(date2, formatter2));
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @GetMapping("/transactionType/{paymentType}")
    public ResponseEntity<Object> getByTransactionType(@PathVariable String paymentType) {
        PaymentType paymentTypeResponse = PaymentType.valueOf(paymentType);
        if (paymentTypeResponse == null) {
            return new ResponseEntity<>("Error this type no exist", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Transaction> transactionList = transactionServiceInter.findByPaymentType(paymentTypeResponse);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }
}

