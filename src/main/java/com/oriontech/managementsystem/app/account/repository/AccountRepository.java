package com.oriontech.managementsystem.app.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oriontech.managementsystem.app.account.enitities.Account;
import com.oriontech.managementsystem.app.account.enums.EAccountRole;
import com.oriontech.managementsystem.app.account.enums.EAccountStatus;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    // @Query("select a from Accounts a where a.status :status")
    List<Account> findByStatus(EAccountStatus status);

    List<Account> findByRole(EAccountRole role);

}
