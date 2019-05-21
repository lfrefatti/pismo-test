package io.pismo.payments.interfaces;

import io.pismo.payments.domain.AccountListWrapper;
import io.pismo.payments.domain.UpdateLimitInput;
import io.pismo.payments.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(@Autowired AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/limits")
    @ResponseStatus(OK)
    public @ResponseBody AccountListWrapper getAccounts(){
        return new AccountListWrapper(accountService.findAll());
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(code = NO_CONTENT)
    public void patch(@PathVariable Integer id, @RequestBody UpdateLimitInput body){
        accountService.updateAvailableCreditLimit(id, body);
    }


}
