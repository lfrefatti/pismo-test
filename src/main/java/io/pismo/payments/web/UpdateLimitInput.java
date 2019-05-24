package io.pismo.payments.web;

import io.pismo.payments.web.Limit;

public class UpdateLimitInput {

    private Limit available_credit_limit;
    private Limit available_withdrawal_limit;

    public Limit getAvailable_credit_limit() {
        return available_credit_limit;
    }

    public void setAvailable_credit_limit(Limit available_credit_limit) {
        this.available_credit_limit = available_credit_limit;
    }

    public Limit getAvailable_withdrawal_limit() {
        return available_withdrawal_limit;
    }

    public void setAvailable_withdrawal_limit(Limit available_withdrawal_limit) {
        this.available_withdrawal_limit = available_withdrawal_limit;
    }
}
