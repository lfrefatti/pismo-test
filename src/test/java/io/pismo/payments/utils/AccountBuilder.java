package io.pismo.payments.utils;

import io.pismo.payments.domain.Account;

public class AccountBuilder {

    private Account account;

    public AccountBuilder(){
        this.account = new Account();
    }

    public AccountBuilder withAccountId(Integer accountId){
        this.account.setAccountId(accountId);
        return this;
    }

    public AccountBuilder withAvailableCreditLimit(Double availableCreditLimit){
        this.account.setAvailableCreditLimit(availableCreditLimit);
        return this;
    }

    public AccountBuilder withAvailableWithdrawalLimit(Double availableWithdrawalLimit){
        this.account.setAvailableWithdrawalLimit(availableWithdrawalLimit);
        return this;
    }

    public Account build(){
        return this.account;
    }

}
