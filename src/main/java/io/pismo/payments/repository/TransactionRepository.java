package io.pismo.payments.repository;

import io.pismo.payments.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM transactions t WHERE t.accountId = :accountId AND t.balance < 0 AND t.operationType <> 'PAGAMENTO'")
    public List<Transaction> findDebitTransactions(@Param("accountId") Integer accountId);

    @Query("SELECT t FROM transactions t WHERE t.accountId = :accountId AND t.balance > 0 AND t.operationType = 'PAGAMENTO' ORDER BY t.eventDate")
    public List<Transaction> findCreditTransactions(@Param("accountId") Integer accountId);
}
