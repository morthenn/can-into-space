package com.into.space.assessment.transfer.service.validation;

import com.into.space.assessment.account.service.validation.AccountValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FundsTransferValidationService {

    private final AccountValidationService accountService;

    //here other validation services regarding Funds Transfer can be autowired like Fraud detection service, Risk service, etc.

    public void validateFundsTransfer(Long senderAccId, Long receiverAccId, BigDecimal amount) {
        accountService.validateAccountForFundsTransfer(senderAccId, receiverAccId, amount);
    }
}
