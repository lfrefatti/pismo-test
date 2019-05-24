package io.pismo.payments.service.impl;

import io.pismo.payments.domain.Account;
import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.domain.Transaction;
import io.pismo.payments.exceptions.InsufficientFundsException;
import io.pismo.payments.exceptions.InsufficientWithdrawalLimitException;
import io.pismo.payments.repository.TransactionRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.service.TransactionBalanceService;
import io.pismo.payments.utils.AccountBuilder;
import io.pismo.payments.utils.TransactionInputBuilder;
import io.pismo.payments.web.TransactionInput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static io.pismo.payments.domain.OperationsTypes.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.MockitoAnnotations.initMocks;

public class TransactionServiceImplTest {

    @Rule
    public ExpectedException expectedException = none();

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionBalanceService transactionBalanceService;

    private TransactionServiceImpl transactionService;

    @Before
    public void setUp(){
        initMocks(this);
        this.transactionService = new TransactionServiceImpl(accountService, transactionRepository, transactionBalanceService);
    }

    @Test
    public void should_throw_InsuficientFundsException_when_transaction_value_is_higher_then_available_limit(){
        expectedException.expect(InsufficientFundsException.class);
        expectedException.expectMessage("Your credit card limit was reached");
        transactionService.checkLimit(buildAccount(), 2000d, COMPRA_A_VISTA);
    }

    @Test
    public void should_return_true_when_transaction_value_is_lower_then_available_limit(){
        assertTrue(transactionService.checkLimit(buildAccount(), 900d, COMPRA_PARCELADA));
    }

    @Test
    public void should_throw_InsuficientFundsException_when_withdrawal_value_is_higher_then_available_limit(){
        expectedException.expect(InsufficientWithdrawalLimitException.class);
        expectedException.expectMessage("Your withdrawal limit was reached");
        transactionService.checkLimit(buildAccount(), 600d, SAQUE);
    }

    @Test
    public void should_return_true_when_withdrawal_value_is_lower_then_available_limit(){
        assertTrue(transactionService.checkLimit(buildAccount(), 300d, SAQUE));
    }

    @Test
    public void should_create_transaction_with_positive_amount_when_operation_is_pagamento(){
        TransactionInput transactionInput = buildTransactionInput(PAGAMENTO);
        Transaction transaction = transactionService.buildTransaction(transactionInput);

        assertEquals(transactionInput.getAmount(), transaction.getAmount());
        assertEquals(transactionInput.getAmount(), transaction.getBalance());
    }

    @Test
    public void should_create_transaction_with_negative_amount_when_operation_is_not_pagamento(){
        TransactionInput transactionInput = buildTransactionInput(SAQUE);
        Transaction transaction = transactionService.buildTransaction(transactionInput);

        Double negAmount = -transactionInput.getAmount();

        assertEquals(negAmount, transaction.getAmount());
        assertEquals(negAmount, transaction.getBalance());
    }


    private TransactionInput buildTransactionInput(OperationsTypes operationsTypes){
        return new TransactionInputBuilder()
                .withAccountId(1)
                .withAmount(2000d)
                .withOperationTypeId(operationsTypes)
                .build();
    }

    private Account buildAccount(){
        return new AccountBuilder()
                .withAccountId(0)
                .withAvailableCreditLimit(1000d)
                .withAvailableWithdrawalLimit(500d)
                .build();
    }

}