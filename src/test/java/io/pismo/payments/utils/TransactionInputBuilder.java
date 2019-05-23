package io.pismo.payments.utils;

import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.web.TransactionInput;

public class TransactionInputBuilder {

    private TransactionInput transactionInput;

    public TransactionInputBuilder() {
        this.transactionInput = new TransactionInput();
    }

    public TransactionInputBuilder withAccountId(Integer accountId){
        this.transactionInput.setAccountId(accountId);
        return this;
    }

    public TransactionInputBuilder withAmount(Double amount){
        this.transactionInput.setAmount(amount);
        return this;
    }

    public TransactionInputBuilder withOperationTypeId(OperationsTypes operationType){
        this.transactionInput.setOperationType(operationType);
        return this;
    }

    public TransactionInput build(){
        return transactionInput;
    }
}
