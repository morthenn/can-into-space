package com.into.space.assessment.transfer.storage.guard.mongo;

import com.into.space.assessment.transfer.domain.TransactionGuard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoTransactionGuardRepository extends ReactiveMongoRepository<TransactionGuard, String> {
}
