package com.into.space.assessment.transfer.storage.business.h2;

import com.into.space.assessment.transfer.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByTransactionGuardId(String transactionGuardId);
}
