package io.pismo.payments.service.impl;

import io.pismo.payments.domain.Account;
import io.pismo.payments.web.UpdateLimitInput;
import io.pismo.payments.exceptions.NotFoundException;
import io.pismo.payments.repository.AccountRepository;
import io.pismo.payments.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(@Autowired AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Integer id){
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public Account updateAvailableCreditLimit(Integer id, UpdateLimitInput values) {
        Account account = this.findById(id);
        account.updateAvailableCreditLimit(values.getAvailable_credit_limit().getAmount());
        account.updateAvailableWithDrawalLimit(values.getAvailable_withdrawal_limit().getAmount());
        accountRepository.save(account);
        return account;
    }

}
