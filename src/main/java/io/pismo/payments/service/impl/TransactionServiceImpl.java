package io.pismo.payments.service.impl;

import io.pismo.payments.domain.*;
import io.pismo.payments.exceptions.InsufficientFundsException;
import io.pismo.payments.exceptions.InsufficientWithdrawalLimitException;
import io.pismo.payments.repository.TransactionRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.service.TransactionService;
import io.pismo.payments.web.TransactionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static io.pismo.payments.domain.OperationsTypes.PAGAMENTO;
import static io.pismo.payments.domain.OperationsTypes.SAQUE;

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
    public void processTransaction(TransactionInput transactionInput) {
        Account account = accountService.findById(transactionInput.getAccountId());

        checkLimit(account, transactionInput.getAmount(), transactionInput.getOperationType());

        Transaction transaction = buildTransaction(transactionInput);
        transactionRepository.save(transaction);

        updateLimit(transaction.getAmount(), transaction.getAccountId(), transaction.getOperationTypeId());
    }

    protected boolean checkLimit(Account account, Double amount, OperationsTypes operationType){
        if (!PAGAMENTO.equals(operationType) && amount > account.getAvailableCreditLimit()){
            throw new InsufficientFundsException();
        }

        if (SAQUE.equals(operationType) && amount > account.getAvailableWithdrawalLimit()){
            throw new InsufficientWithdrawalLimitException();
        }

        return true;
    }

    private void updateLimit(Double amount, Integer id, Integer operationId){
        if (PAGAMENTO.getOperationTypeId().equals(operationId)){
            return;
        }

        Limit creditLimit = new Limit(amount);

        Limit withdrawalLimit = new Limit(0d);
        if (SAQUE.getOperationTypeId().equals(operationId)){
            withdrawalLimit.setAmount(amount);
        }

        UpdateLimitInput updateLimitInput = new UpdateLimitInput();
        updateLimitInput.setAvailable_credit_limit(creditLimit);
        updateLimitInput.setAvailable_withdrawal_limit(withdrawalLimit);

        accountService.updateAvailableCreditLimit(id, updateLimitInput);
    }

    protected Transaction buildTransaction(TransactionInput input) {
        Double amount = getSignedAmount(input);

        Transaction transaction = new Transaction();
        transaction.setAccountId(input.getAccountId());
        transaction.setOperationTypeId(input.getOperationType().getOperationTypeId());
        transaction.setAmount(amount);
        transaction.setBalance(amount);
        transaction.setEventDate(LocalDate.now());
        transaction.setDueDate(transaction.getEventDate().plusDays(10));
        return transaction;
    }

    private double getSignedAmount(TransactionInput input) {
        return PAGAMENTO.equals(input.getOperationType()) ? input.getAmount() : - input.getAmount();
    }

}
