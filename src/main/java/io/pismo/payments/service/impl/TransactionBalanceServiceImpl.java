package io.pismo.payments.service.impl;

import io.pismo.payments.web.Limit;
import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.domain.Transaction;
import io.pismo.payments.web.UpdateLimitInput;
import io.pismo.payments.repository.TransactionRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.service.TransactionBalanceService;
import io.pismo.payments.utils.TransactonComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class TransactionBalanceServiceImpl implements TransactionBalanceService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionBalanceServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public void updateTransactionBalance(Integer accountId){
        List<Transaction> debitTransactions = transactionRepository.findDebitTransactions(accountId);
        List<Transaction> creditTransactions = transactionRepository.findCreditTransactions(accountId);

        Collections.sort(debitTransactions, new TransactonComparator());

        List<Transaction> transactions = new ArrayList<>();

        Double creditValue = 0d;
        Double withdrawal = 0d;

        for (Transaction credit : creditTransactions) {
            while (credit.getBalance() > 0 && !debitTransactions.isEmpty()){
                for (Iterator<Transaction> iterator = debitTransactions.iterator(); iterator.hasNext();) {
                    Transaction debit =  iterator.next();
                    if (credit.getBalance() >= Math.abs(debit.getBalance())){
                        creditValue += Math.abs(debit.getBalance());
                        if (OperationsTypes.SAQUE.getOperationTypeId().equals(debit.getOperationType())){
                            withdrawal += Math.abs(debit.getBalance());
                        }
                        credit.setBalance(credit.getBalance() + debit.getBalance());
                        debit.setBalance(0d);
                        transactions.add(debit);
                        iterator.remove();
                    }
                    else {
                        creditValue += Math.abs(credit.getBalance());
                        if (OperationsTypes.SAQUE.getOperationTypeId().equals(debit.getOperationType())){
                            withdrawal += Math.abs(credit.getBalance());
                        }
                        debit.setBalance(debit.getBalance() + credit.getBalance());
                        credit.setBalance(0d);
                    }
                }
            }
        }

        transactionRepository.saveAll(creditTransactions);
        transactionRepository.saveAll(transactions);
        updateLimit(creditValue, withdrawal, accountId);
    }

    private void updateLimit(Double creditLimit, Double withdrawalLimit, Integer accountId){
        UpdateLimitInput updateLimitInput = new UpdateLimitInput();
        updateLimitInput.setAvailable_credit_limit(new Limit(creditLimit));
        updateLimitInput.setAvailable_withdrawal_limit(new Limit(withdrawalLimit));

        accountService.updateAvailableCreditLimit(accountId, updateLimitInput);
    }

}
