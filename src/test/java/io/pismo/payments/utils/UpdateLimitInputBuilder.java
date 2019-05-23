package io.pismo.payments.utils;

import io.pismo.payments.domain.Limit;
import io.pismo.payments.domain.UpdateLimitInput;

public class UpdateLimitInputBuilder {

    private UpdateLimitInput updateLimitInput;

    public UpdateLimitInputBuilder(){
        this.updateLimitInput = new UpdateLimitInput();
    }

    public UpdateLimitInputBuilder withAvailableCreditLimit(Double amount){
        updateLimitInput.setAvailable_credit_limit(getLimit(amount));
        return this;
    }

    public UpdateLimitInputBuilder withAvailableWithdrawalLimit(Double amount){
        updateLimitInput.setAvailable_withdrawal_limit(getLimit(amount));
        return this;
    }

    public UpdateLimitInput build(){
        return this.updateLimitInput;
    }

    private Limit getLimit(Double amount) {
        Limit limit = new Limit(amount);
        return limit;
    }

}
