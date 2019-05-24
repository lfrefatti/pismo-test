package io.pismo.payments.service;

import io.pismo.payments.web.PaymentInput;
import io.pismo.payments.web.TransactionInput;

import java.util.List;

public interface TransactionService {

    void processTransaction(TransactionInput transactionInput);

    void processPaymentTransactions(List<PaymentInput> payments);

}
