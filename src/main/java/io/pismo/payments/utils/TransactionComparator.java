package io.pismo.payments.utils;

import io.pismo.payments.domain.Transaction;

import java.util.Comparator;

public class TransactionComparator implements Comparator<Transaction> {

    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t1.compareTo(t2);
    }

}
