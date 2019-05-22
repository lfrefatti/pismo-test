package io.pismo.payments.interfaces;

import io.pismo.payments.service.TransactionService;
import io.pismo.payments.web.TransactionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(@Autowired TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void postTransaction(@RequestBody TransactionInput transactionInput){
        transactionService.createTransaction(transactionInput);
    }

}
