package com.into.space.assessment.transfer.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.into.space.assessment.transfer.domain.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
@Data
@AllArgsConstructor
public class TransactionGuard {

    @Id
    String id;

    Status status;

    @JsonInclude(Include.NON_NULL)
    String description;

    public static TransactionGuard createWithStatus(Status status) {
        return new TransactionGuard(null, status, null);
    }
}
