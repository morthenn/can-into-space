package com.into.space.assessment.transfer.api;

import com.into.space.assessment.transfer.api.command.FundsTransferCommand;
import com.into.space.assessment.transfer.domain.TransactionGuard;
import com.into.space.assessment.transfer.service.FundsTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Slf4j
public class FundsController {

    private final FundsTransfer fundsTransfer;

    //Normally I would probably get sender's userId from JWT Token and then validate against payload
    //Also assuming that we don't need currency in payload because we exchange against targetAccount currency
    @PostMapping(value = "/transfer-funds",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<TransactionGuard>> transferFunds(@Valid @RequestBody FundsTransferCommand fundsTransferCommand) {
        try {
            log.info("Received HTTP Post call for {}", fundsTransferCommand);
            return ResponseEntity.status(HttpStatus.CREATED).body(fundsTransfer.transfer(fundsTransferCommand));
        } catch (Exception ex) {
            log.error("Error occurred while starting funds transfer");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
