package com.into.space.assessment.transfer.service;

import com.into.space.assessment.transfer.domain.Transaction;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.storage.business.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void create(Transaction transactionStart) {
        log.info("Handling TransactionStartedEvent in TransactionLifecycleHandler");
        transactionRepository.save(transactionStart);
        log.info("Transaction [{} - {}] - STARTED ", transactionStart.getOperation(), transactionStart.getId());
    }

    @Transactional
    public void update(String id, Status newStatus) {
        log.info("Handling TransactionStartedEvent in TransactionLifecycleHandler");
        Transaction transaction = transactionRepository.getByTransactionGuardId(id);
        Status previousStatus = transaction.getStatus();
        transaction.setStatus(newStatus);
        transactionRepository.save(transaction);
        log.info("Transaction [{} - {}] -  Updated status - from {}, to {}",
                transaction.getOperation(), transaction.getId(), previousStatus, transaction.getStatus());

    }
}
