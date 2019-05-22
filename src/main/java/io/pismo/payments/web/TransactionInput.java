package io.pismo.payments.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionInput {

    @JsonProperty(value = "account_id")
    private Integer accountId;

    @JsonProperty(value = "operation_type_id")
    private Integer operationTypeId;

    @JsonProperty(value = "amount")
    private Double amount;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
