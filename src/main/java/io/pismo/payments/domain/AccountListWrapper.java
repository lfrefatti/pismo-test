package io.pismo.payments.domain;

import java.util.List;

public class AccountListWrapper {

    private List<Account> limits;

    public AccountListWrapper(List<Account> limits) {
        this.limits = limits;
    }

    public List<Account> getLimits() {
        return limits;
    }

    public void setLimits(List<Account> limits) {
        this.limits = limits;
    }
}
