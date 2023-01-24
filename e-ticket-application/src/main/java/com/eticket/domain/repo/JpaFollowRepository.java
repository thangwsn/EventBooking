package com.eticket.domain.repo;

import com.eticket.domain.entity.event.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaFollowRepository extends JpaRepository<Follow, Integer> {
    Optional<Follow> findByUserIdAndEventId(Integer userId, Integer eventId);

    Optional<Follow> findByUserIdAndEventIdAndRemovedFalse(Integer userId, Integer eventId);

    Integer countByEventIdAndRemovedFalse(Integer eventId);

    Integer countByUserIdAndRemovedFalse(Integer userId);

    List<Follow> findByUserIdAndRemovedFalse(Integer userId);
}
