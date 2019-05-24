package io.pismo.payments.interfaces;

import io.pismo.payments.service.TransactionService;
import io.pismo.payments.web.PaymentInput;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    private final TransactionService transactionService;

    public PaymentsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void post(@RequestBody List<PaymentInput> payments){
        transactionService.processPaymentTransactions(payments);
    }

}
