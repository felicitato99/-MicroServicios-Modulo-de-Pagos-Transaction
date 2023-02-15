package com.accenture.modulosPago.services;

import com.accenture.modulosPago.entities.Transaction;
import com.accenture.modulosPago.dtos.TransactionDto;
import com.accenture.modulosPago.entities.PaymentType;
import com.accenture.modulosPago.models.Account;
import com.accenture.modulosPago.models.User;
import com.accenture.modulosPago.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("transactionServiceRestTemplate")
public class TransactionService implements TransactionServiceInter{
    @Autowired
    RestTemplate clientRest;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(TransactionDto transactionDTO) {
        Transaction transaction = new Transaction(transactionDTO);
        clientRest.postForEntity("http://localhost:8002/api/account/updateBalance", transactionDTO, Account.class);
        return transactionRepository.save(transaction);
    }

    @Override
    public Boolean checkAccountNoExist(String accountNumber) {
        Map<String,String> pathVariables = new HashMap<>();
        pathVariables.put("number",accountNumber.toString());
        System.out.println(pathVariables);
        Account account = clientRest.getForObject("http://localhost:8002/api/account/list/number/{number}",Account.class,pathVariables);
        if(account!= null){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Account> checkUserNoExist(String idUser) {
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",idUser);
        User user = clientRest.getForObject("http://localhost:8001/api/user/list/{id}",User.class,pathVariables);
        if(user!= null){
            Map<String,String> pathVariables1 = new HashMap<String,String>();
            pathVariables1.put("idUser",user.getId().toString());
            List<Account> accountList = Arrays.stream(clientRest.getForObject("http://localhost:8002/api/account/listAccount/{idUser}",Account[].class,pathVariables1)).toList();
            return accountList;
        } else {
            return null;
        }
    }

    @Override
    public Double getBalanceSendingAccount(String accountNumber) {
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("number",accountNumber);
        Account account = clientRest.getForObject("http://localhost:8002/api/account/list/number/{number}",Account.class,pathVariables);
        return account.getBalance().doubleValue();
    }

    @Override
    public String getNameUserAccount(String accountNumber) {
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("number",accountNumber);
        Account account = clientRest.getForObject("http://localhost:8002/api/account/list/number/{number}",Account.class,pathVariables);
        Map<String,String> pathVariables1 = new HashMap<String,String>();
        pathVariables1.put("id",account.getUserId().toString());
        User user = clientRest.getForObject("http://localhost:8001/api/user/list/{id}",User.class,pathVariables1);
        return user.getName() + " "+ user.getLastname();
    }

    @Override
    public List<Transaction> findAllTransaction() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findByIdTransaction(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> findByDateBetween(LocalDateTime date1, LocalDateTime date2) {
        return transactionRepository.findByTransactionDateBetween(date1,date2);
    }

    @Override
    public List<Transaction> findTransactionsUserByAccountNumber(String accountNumber) {
        return transactionRepository.findBySendingAccountOrReceiverAccount(accountNumber, accountNumber);
    }

    @Override
    public List<Transaction> findByPaymentType(PaymentType paymentType) {
        return transactionRepository.findByPaymentType(paymentType);
    }


}

