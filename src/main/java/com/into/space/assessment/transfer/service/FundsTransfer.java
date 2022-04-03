package com.into.space.assessment.transfer.service;

import com.into.space.assessment.transfer.api.command.FundsTransferCommand;
import com.into.space.assessment.transfer.domain.TransactionGuard;
import reactor.core.publisher.Mono;

public interface FundsTransfer {

    Mono<TransactionGuard> transfer(FundsTransferCommand fundsTransferCommand);
}
