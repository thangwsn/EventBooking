package com.eticket.domain.repo;

import com.eticket.domain.entity.quartz.ScheduleJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaScheduleJobRepository extends JpaRepository<ScheduleJob, Integer> {
    Optional<ScheduleJob> findByRemovedFalseAndObjectIdAndType(Integer integer, String type);

    Optional<ScheduleJob> findByRemovedFalseAndJobKey(String jobKey);
}
