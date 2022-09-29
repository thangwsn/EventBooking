package com.eticket.domain.repo;

import com.eticket.domain.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsernameAndIsActivedTrueAndRemovedFalse(String username);
}
