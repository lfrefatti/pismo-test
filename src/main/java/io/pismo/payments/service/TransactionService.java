package io.pismo.payments.service;

import io.pismo.payments.web.TransactionInput;

public interface TransactionService {

    void createTransaction(TransactionInput transactionInput);

}
