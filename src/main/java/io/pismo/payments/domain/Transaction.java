package io.pismo.payments.domain;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "transactions")
public class Transaction implements Comparable{

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "Transaction_ID")
    private Integer transactionId;

    @Column(name = "Account_ID")
    private Integer accountId;

    @Column(name = "OperationType_ID")
    @Enumerated(EnumType.STRING)
    private OperationsTypes operationType;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "Balance")
    private Double balance;

    @Column(name = "EventDate")
    private LocalDate eventDate;

    @Column(name = "DueDate")
    private LocalDate dueDate;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public OperationsTypes getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationsTypes operationType) {
        this.operationType = operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public int compareTo(Object o) {
        Transaction other = (Transaction) o;
        if (this.getOperationType().getChargeOrder() < other.getOperationType().getChargeOrder()){
            return -1;
        } else if (this.getOperationType().getChargeOrder() < other.getOperationType().getChargeOrder()){
            return 1;
        } else {
            if (this.getEventDate().isBefore(other.eventDate)){
                return -1;
            } else if (this.getEventDate().isAfter(other.getEventDate())){
                return 1;
            }
        }

        return 0;
    }
}
