package io.pismo.payments.service;

import io.pismo.payments.domain.Transaction;

import java.util.List;

public interface TransactionBalanceService {

    List<Transaction> updateTransactionBalance(Integer accountId);

}
