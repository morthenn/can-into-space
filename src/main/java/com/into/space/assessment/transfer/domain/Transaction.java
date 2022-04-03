package com.into.space.assessment.transfer.domain;

import com.into.space.assessment.transfer.domain.enumeration.Operation;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Data
public class Transaction {

    @Id
    @GeneratedValue
    Long id;

    String transactionGuardId;

    Long fromAccountId;

    Long toAccountId;

    LocalDateTime startDateTime;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    Operation operation;

    @Enumerated(EnumType.STRING)
    @Setter
    Status status;
}
