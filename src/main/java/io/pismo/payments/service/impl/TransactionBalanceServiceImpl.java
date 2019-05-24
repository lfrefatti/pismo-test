package io.pismo.payments.service.impl;

import io.pismo.payments.web.Limit;
import io.pismo.payments.domain.Transaction;
import io.pismo.payments.web.UpdateLimitInput;
import io.pismo.payments.repository.TransactionRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.service.TransactionBalanceService;
import io.pismo.payments.utils.TransactionComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static io.pismo.payments.domain.OperationsTypes.isWithdrawal;

@Service
public class TransactionBalanceServiceImpl implements TransactionBalanceService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionBalanceServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public List<Transaction> updateTransactionBalance(Integer accountId){
        List<Transaction> debitTransactions = getDebitTransactions(accountId);
        List<Transaction> creditTransactions = getCreditTransactions(accountId);

        List<Transaction> transactionsToUpdate = new ArrayList<>();

        Double creditValue = 0d;
        Double withdrawal = 0d;

        for (Transaction credit : creditTransactions) {
            while (credit.getBalance() > 0 && !debitTransactions.isEmpty()){
                for (Iterator<Transaction> iterator = debitTransactions.iterator(); iterator.hasNext();) {

                    Transaction debit =  iterator.next();
                    Double discountedAmount = 0d;

                    if (credit.getBalance() >= Math.abs(debit.getBalance())){
                        discountedAmount = Math.abs(debit.getBalance());
                        credit.setBalance(credit.getBalance() + debit.getBalance());
                        debit.setBalance(0d);
                        transactionsToUpdate.add(debit);
                        iterator.remove();
                    }
                    else {
                        discountedAmount = Math.abs(credit.getBalance());
                        debit.setBalance(debit.getBalance() + credit.getBalance());
                        transactionsToUpdate.add(debit);
                        credit.setBalance(0d);
                    }

                    creditValue += discountedAmount;
                    if (isWithdrawal(debit.getOperationType())){
                        withdrawal += discountedAmount;
                    }
                }
            }
            transactionsToUpdate.add(credit);
        }

        saveTransactions(transactionsToUpdate);
        updateLimit(creditValue, withdrawal, accountId);

        return transactionsToUpdate;
    }

    private void saveTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    private List<Transaction> getDebitTransactions(Integer accountId){
        List<Transaction> debitTransactions = transactionRepository.findDebitTransactions(accountId);
        sortTransactions(debitTransactions);
        return debitTransactions;
    }

    private List<Transaction> getCreditTransactions(Integer accountId){
        return transactionRepository.findCreditTransactions(accountId);
    }

    private void sortTransactions(List<Transaction> transactions){
        Collections.sort(transactions, new TransactionComparator());
    }

    private void updateLimit(Double creditLimit, Double withdrawalLimit, Integer accountId){
        UpdateLimitInput updateLimitInput = new UpdateLimitInput();
        updateLimitInput.setAvailable_credit_limit(new Limit(creditLimit));
        updateLimitInput.setAvailable_withdrawal_limit(new Limit(withdrawalLimit));

        accountService.updateAvailableCreditLimit(accountId, updateLimitInput);
    }

}
