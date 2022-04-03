package com.into.space.assessment.transfer.storage.business.h2;

import com.into.space.assessment.account.domain.Account;
import com.into.space.assessment.account.domain.AccountValidationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2AccountRepository extends JpaRepository<Account, Long> {

    AccountValidationView getAccountBalanceByAccountId(Long accountId);
}
