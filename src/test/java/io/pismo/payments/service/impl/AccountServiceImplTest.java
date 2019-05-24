package io.pismo.payments.service.impl;

import io.pismo.payments.domain.Account;
import io.pismo.payments.web.UpdateLimitInput;
import io.pismo.payments.exceptions.NotFoundException;
import io.pismo.payments.repository.AccountRepository;
import io.pismo.payments.service.AccountService;
import io.pismo.payments.utils.AccountBuilder;
import io.pismo.payments.utils.UpdateLimitInputBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountServiceImplTest {

    @Mock
    AccountRepository accountRepository;

    AccountService accountService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp(){
        initMocks(this);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void should_throw_NotFoundException_when_tries_to_find_nonexistent_account(){
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Account not found");
        when(accountRepository.findById(anyInt())).thenReturn(empty());
        accountService.findById(0);
    }

    @Test
    public void should_retutn_account_object_when_tries_to_find_existent_account(){
        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(new Account()));
        assertNotNull(accountService.findById(0));
    }

    @Test
    public void should_return_empty_list_when_tries_to_find_accounts_but_there_is_none(){
        when(accountRepository.findAll()).thenReturn(new ArrayList<>());
        List<Account> accounts = accountService.findAll();
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void should_return_account_whit_new_available_limits_when_update_limits(){
        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(buildAccount()));
        Account account = accountService.updateAvailableCreditLimit(0, buildUpdateLimitInput());
        assertEquals("Expected credit limit", Double.valueOf(700d), account.getAvailableCreditLimit());
        assertEquals("Expected withdrawal limit", Double.valueOf(550d), account.getAvailableWithdrawalLimit());

    }

    private Account buildAccount(){
        return new AccountBuilder()
                .withAccountId(0)
                .withAvailableCreditLimit(1000d)
                .withAvailableWithdrawalLimit(500d)
                .build();
    }

    private UpdateLimitInput buildUpdateLimitInput(){
        return new UpdateLimitInputBuilder()
                .withAvailableCreditLimit(-300d)
                .withAvailableWithdrawalLimit(50d)
                .build();
    }

}