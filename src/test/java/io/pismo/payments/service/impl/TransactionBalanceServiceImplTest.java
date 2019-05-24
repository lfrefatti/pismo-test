package io.pismo.payments.service.impl;

import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.domain.Transaction;
import io.pismo.payments.repository.TransactionRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.service.TransactionBalanceService;
import io.pismo.payments.utils.TransactionBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.pismo.payments.domain.OperationsTypes.*;
import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TransactionBalanceServiceImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionBalanceService transactionBalanceService;

    @Before
    public void setUp(){
        initMocks(this);
        this.transactionBalanceService = new TransactionBalanceServiceImpl(transactionRepository, accountService);
    }

    @Test
    public void should_return_a_list_with_updated_withdrawal_and_payment_transactions(){
        when(transactionRepository.findCreditTransactions(anyInt())).thenReturn(buildCreditTransactions(70d));
        when(transactionRepository.findDebitTransactions(anyInt())).thenReturn(buildDebitTransactions());
        List<Transaction> transactions = transactionBalanceService.updateTransactionBalance(1);
        assertEquals(Double.valueOf(-30), transactions.get(0).getBalance());
        assertEquals(Double.valueOf(0), transactions.get(3).getBalance());
    }

    @Test
    public void should_return_a_list_with_four_transactions_with_positive_payment_balance(){
        when(transactionRepository.findCreditTransactions(anyInt())).thenReturn(buildCreditTransactions(350d));
        when(transactionRepository.findDebitTransactions(anyInt())).thenReturn(buildDebitTransactions());
        List<Transaction> transactions = transactionBalanceService.updateTransactionBalance(1);
        assertEquals(Double.valueOf(0), transactions.get(0).getBalance());
        assertEquals(Double.valueOf(0), transactions.get(1).getBalance());
        assertEquals(Double.valueOf(0), transactions.get(2).getBalance());
        assertEquals(Double.valueOf(50), transactions.get(3).getBalance());
    }

    @Test
    public void should_return_a_list_with_four_transactions_with_zero_payment_balance(){
        when(transactionRepository.findCreditTransactions(anyInt())).thenReturn(buildCreditTransactions(300d));
        when(transactionRepository.findDebitTransactions(anyInt())).thenReturn(buildDebitTransactions());
        List<Transaction> transactions = transactionBalanceService.updateTransactionBalance(1);
        assertEquals(Double.valueOf(0), transactions.get(0).getBalance());
        assertEquals(Double.valueOf(0), transactions.get(1).getBalance());
        assertEquals(Double.valueOf(0), transactions.get(2).getBalance());
        assertEquals(Double.valueOf(0), transactions.get(3).getBalance());
    }

    private List<Transaction> buildDebitTransactions(){
        Transaction saque = buildTransaction(-100d, SAQUE);
        Transaction compraParcelada = buildTransaction(-100d, COMPRA_PARCELADA);
        Transaction compraAVista = buildTransaction(-100d, COMPRA_A_VISTA);
        return new ArrayList<>(Arrays.asList(compraParcelada, saque, compraAVista));
    }

    private List<Transaction> buildCreditTransactions(Double amount){
        return Arrays.asList(buildTransaction(amount, PAGAMENTO));
    }

    private Transaction buildTransaction(Double amount, OperationsTypes operationType){
        return new TransactionBuilder()
                .withAmount(amount)
                .withBalance(amount)
                .withOperationType(operationType)
                .withEventDate(LocalDate.now())
                .build();
    }
}