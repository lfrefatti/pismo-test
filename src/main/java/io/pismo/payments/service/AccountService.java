package io.pismo.payments.service;

import io.pismo.payments.domain.Account;
import io.pismo.payments.web.UpdateLimitInput;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(Integer id);

    Account updateAvailableCreditLimit(Integer id, UpdateLimitInput values);

}
