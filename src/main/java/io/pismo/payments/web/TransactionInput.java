package io.pismo.payments.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.pismo.payments.domain.OperationsTypes;
import io.pismo.payments.utils.OperationsTypesDeserializer;

public class TransactionInput {

    @JsonProperty(value = "account_id")
    private Integer accountId;

    @JsonProperty(value = "operation_type_id")
    @JsonDeserialize(using = OperationsTypesDeserializer.class)
    private OperationsTypes operationType;

    @JsonProperty(value = "amount")
    private Double amount;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public OperationsTypes getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationsTypes operationType) {
        this.operationType = operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
