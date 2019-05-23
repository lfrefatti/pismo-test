package io.pismo.payments.domain;

public class Limit {

    private Double amount;

    public Limit(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
