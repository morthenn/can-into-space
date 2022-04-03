package com.into.space.assessment.transfer.api.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FundsTransferCommand {

    @NotNull(message = "Sender account Id is mandatory")
    Long senderAccountId;

    @NotNull(message = "Receiver account Id is mandatory")
    Long receiverAccountId;

    @NotNull(message = "Amount is mandatory")
    @Positive
    BigDecimal amount;
}
