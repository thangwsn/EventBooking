package com.eticket.domain.repo;

import com.eticket.domain.entity.event.Event;
import com.eticket.domain.entity.event.EventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaEventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrganizerIdAndRemovedFalse(Integer organizerId);

    Optional<Event> findByIdAndRemovedFalse(Integer eventId);

    List<Event> findByRemovedFalseOrderByCreateTimeDesc();

    Optional<Event> findByIdAndRemovedFalseAndStatusIsNot(Integer eventId, EventStatus status);

    List<Event> findByRemovedFalseAndStatus(EventStatus status, Pageable pageable);

    List<Event> findByRemovedFalseAndStatusAndTypeOrderByStartTimeAsc(Event status, String type, Pageable pageable);
}
