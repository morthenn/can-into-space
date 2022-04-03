package com.into.space.assessment.transfer.storage.guard.adapter;

import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import com.into.space.assessment.transfer.storage.guard.TransactionGuardRepository;
import com.into.space.assessment.transfer.storage.guard.mongo.MongoTransactionGuardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MongoTransactionGuardDatabaseAdapter implements TransactionGuardRepository {

    private final MongoTransactionGuardRepository mongoTransactionGuardRepository;

    public Mono<TransactionGuard> save(TransactionGuard transactionGuard) {
        return mongoTransactionGuardRepository.save(transactionGuard);
    }

    @Override
    public Mono<Status> getStatusById(String id) {
        return mongoTransactionGuardRepository.findById(id).map(TransactionGuard::getStatus);
    }

    @Override
    public Mono<TransactionGuard> getById(String id) {
        return mongoTransactionGuardRepository.findById(id);
    }

    @Override
    public Flux<TransactionGuard> findAll() {
        return mongoTransactionGuardRepository.findAll();
    }

    @Override
    public Mono<Void> deleteAll() {
        return mongoTransactionGuardRepository.deleteAll();
    }
}
