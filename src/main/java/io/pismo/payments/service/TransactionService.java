package io.pismo.payments.service;

import io.pismo.payments.web.TransactionInput;

public interface TransactionService {

    void processTransaction(TransactionInput transactionInput);

}
