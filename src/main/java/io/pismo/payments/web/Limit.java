package io.pismo.payments.web;

public class Limit {

    private Double amount;

    public Limit(Double amount) {
        this.amount = amount;
    }

    public Limit(){}

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
