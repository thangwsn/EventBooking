package com.eticket.domain.repo;

import com.eticket.domain.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndEmailAndRemovedFalse(String username, String email);

    @Query(nativeQuery = true, value = "SELECT u.userCode FROM User u ORDER BY u.userCode DESC LIMIT 1")
    Optional<String> latestUserCode();

    Optional<User> findByUsernameAndRemovedFalse(String username);
}
