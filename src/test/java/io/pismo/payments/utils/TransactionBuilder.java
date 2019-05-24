package io.pismo.payments.utils;

import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.domain.Transaction;

import java.time.LocalDate;

public class TransactionBuilder {

    private Transaction transaction;

    public TransactionBuilder() {
        this.transaction = new Transaction();
    }

    public TransactionBuilder withOperationType(OperationsTypes operationType){
        this.transaction.setOperationType(operationType);
        return this;
    }

    public TransactionBuilder withAmount(Double amount){
        this.transaction.setAmount(amount);
        return this;
    }

    public TransactionBuilder withBalance(Double balance){
        this.transaction.setBalance(balance);
        return this;
    }

    public TransactionBuilder withEventDate(LocalDate eventDate){
        this.transaction.setEventDate(eventDate);
        return this;
    }

    public Transaction build(){
        return this.transaction;
    }
}
