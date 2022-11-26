package com.eticket.domain.repo;

import com.eticket.domain.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndEmailAndRemovedFalse(String username, String email);

    @Query(nativeQuery = true, value = "SELECT users.user_code FROM users WHERE user_code IS NOT NULL ORDER BY user_code DESC LIMIT 1")
    Optional<String> latestUserCode();

    Optional<User> findByUsernameAndRemovedFalse(String username);

    Optional<User> findByIdAndActiveCode(Integer userId, String activeCode);

    List<User> findByRemovedFalse();

    Optional<User> findByIdAndRemovedFalse(Integer userId);
}
