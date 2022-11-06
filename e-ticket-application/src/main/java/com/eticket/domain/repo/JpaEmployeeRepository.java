package com.eticket.domain.repo;

import com.eticket.domain.entity.account.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaEmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(nativeQuery = true, value = "SELECT e.employee_code FROM Employee e ORDER BY e.employee_code DESC LIMIT 1")
    Optional<String> latestEmployeeCode();

    Optional<Employee> findByUsernameAndRemovedFalse(String username);
}
