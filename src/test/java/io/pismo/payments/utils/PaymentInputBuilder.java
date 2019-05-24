package io.pismo.payments.utils;

import io.pismo.payments.web.PaymentInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentInputBuilder {

    private PaymentInput paymentInput;

    public PaymentInputBuilder() {
        this.paymentInput = new PaymentInput();
    }

    public PaymentInputBuilder withAccountId(Integer accountId){
        this.paymentInput.setAccountId(accountId);
        return this;
    }

    public PaymentInputBuilder withAmount(Double amount){
        this.paymentInput.setAmount(amount);
        return this;
    }

    public List<PaymentInput> buildList(){
        return new ArrayList<>(Arrays.asList(paymentInput));
    }
}
