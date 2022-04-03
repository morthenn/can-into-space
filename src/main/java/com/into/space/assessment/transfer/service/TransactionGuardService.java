package com.into.space.assessment.transfer.service;

import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.storage.guard.TransactionGuardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionGuardService {

    private final TransactionGuardRepository transactionGuardRepository;

    @Transactional
    public Mono<TransactionGuard> registerNewTransaction() {
        return transactionGuardRepository.save(TransactionGuard.createWithStatus(Status.STARTED))
                .flatMap(transactionGuard -> {
                    log.info("Registering new transaction guard with id {}", transactionGuard.getId());
                    return Mono.just(transactionGuard);
                });
    }

    @Transactional
    public Mono<TransactionGuard> updateTransactionGuardStatus(String id, Status status, String description) {
        Mono<TransactionGuard> transactionGuardMono = transactionGuardRepository.getById(id)
                .map(transactionGuard -> {
                    Status previousStatus = transactionGuard.getStatus();
                    transactionGuard.setStatus(status);
                    transactionGuard.setDescription(description);
                    log.info("Changed status for transactionGuardId {}, from {}, to {}", transactionGuard.getId(), previousStatus, transactionGuard.getStatus());
                    return transactionGuard;
                }).flatMap(transactionGuardRepository::save);
        transactionGuardMono.subscribe();
        return transactionGuardMono;
    }
}
