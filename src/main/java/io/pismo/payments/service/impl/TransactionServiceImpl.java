package io.pismo.payments.service.impl;

import io.pismo.payments.domain.Account;
import io.pismo.payments.domain.Limit;
import io.pismo.payments.domain.Transaction;
import io.pismo.payments.domain.UpdateLimitInput;
import io.pismo.payments.exceptions.InsufficientFundsException;
import io.pismo.payments.repository.TransactionRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.service.TransactionService;
import io.pismo.payments.web.TransactionInput;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void createTransaction(TransactionInput transactionInput) {
        Account account = accountService.findById(transactionInput.getAccountId());
        checkLimit(account, transactionInput.getAmount());
        Transaction transaction = buildTransaction(transactionInput);
        transactionRepository.save(transaction);
        updateLimit(transaction.getAmount(), transaction.getAccountId());
    }

    private boolean checkLimit(Account account, Double amount){
        if (amount > account.getAvailableCreditLimit()){
            throw new InsufficientFundsException();
        }

        return true;
    }

    private void updateLimit(Double amount, Integer id){
        Limit limit = new Limit();
        limit.setAmount(-amount);

        Limit limit2 = new Limit();
        limit2.setAmount(0d);

        UpdateLimitInput updateLimitInput = new UpdateLimitInput();
        updateLimitInput.setAvailable_credit_limit(limit);
        updateLimitInput.setAvailable_withdrawal_limit(limit2);

        accountService.updateAvailableCreditLimit(id, updateLimitInput);
    }

    private Transaction buildTransaction(TransactionInput input) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(input.getAccountId());
        transaction.setOperationTypeId(input.getOperationTypeId());
        transaction.setAmount(input.getAmount());
        transaction.setBalance(input.getAmount());
        transaction.setEventDate(LocalDate.now());
        transaction.setDueDate(transaction.getEventDate().plusDays(10));
        return transaction;
    }

}
