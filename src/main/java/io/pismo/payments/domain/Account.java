package io.pismo.payments.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "account")
public class Account {

    @Id
    private Integer accountId;
    private Double availableCreditLimit;
    private Double availableWithdrawalLimit;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Double getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    public void setAvailableCreditLimit(Double availableCreditLimit) {
        this.availableCreditLimit = availableCreditLimit;
    }

    public Double getAvailableWithdrawalLimit() {
        return availableWithdrawalLimit;
    }

    public void setAvailableWithdrawalLimit(Double availableWithdrawalLimit) {
        this.availableWithdrawalLimit = availableWithdrawalLimit;
    }

    public void updateAvailableCreditLimit(Double value){
        this.availableCreditLimit += value;
    }

    public void updateAvailableWithDrawalLimit(Double value){
        this.availableWithdrawalLimit += value;
    }
}
