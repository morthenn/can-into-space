package com.into.space.assessment.transfer.storage.guard;

import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface TransactionGuardRepository {

    Mono<TransactionGuard> save(TransactionGuard transactionGuard);

    Mono<Status> getStatusById(String id);

    Mono<TransactionGuard> getById(String id);

    Flux<TransactionGuard> findAll();


    Mono<Void> deleteAll();
}
