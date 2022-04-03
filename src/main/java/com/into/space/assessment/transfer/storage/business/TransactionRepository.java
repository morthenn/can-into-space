package com.into.space.assessment.transfer.storage.business;

import com.into.space.assessment.transfer.domain.Transaction;

public interface TransactionRepository {


    Transaction save(Transaction transaction);

    Transaction getByTransactionGuardId(String transactionGuardId);
}
