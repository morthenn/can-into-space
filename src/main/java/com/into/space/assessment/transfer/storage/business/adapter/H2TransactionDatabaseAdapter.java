package com.into.space.assessment.transfer.storage.business.adapter;


import com.into.space.assessment.transfer.domain.Transaction;
import com.into.space.assessment.transfer.storage.business.TransactionRepository;
import com.into.space.assessment.transfer.storage.business.h2.H2TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2TransactionDatabaseAdapter implements TransactionRepository {

    private final H2TransactionRepository h2TransactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return h2TransactionRepository.save(transaction);
    }

    @Override
    public Transaction getByTransactionGuardId(String transactionGuardId) {
        return h2TransactionRepository.findByTransactionGuardId(transactionGuardId);
    }
}
