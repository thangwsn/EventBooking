package com.eticket.domain.repo;

import com.eticket.domain.entity.event.Event;
import com.eticket.domain.entity.event.EventStatus;
import com.eticket.domain.entity.event.EventType;
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

    List<Event> findByRemovedFalseAndTitleContaining(String title, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndStatus(String title, EventStatus status, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndType(String title, EventType type, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndStatusAndType(String title, EventStatus status, EventType type, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndOrganizerId(String title, Integer organizerId, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndStatusAndOrganizerId(String title, EventStatus status, Integer organizerId, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndTypeAndOrganizerId(String title, EventType type, Integer organizerId, Pageable pageable);

    List<Event> findByRemovedFalseAndTitleContainingAndStatusAndTypeAndOrganizerId(String title, EventStatus status, EventType type, Integer organizerId, Pageable pageable);

    List<Event> findByRemovedFalseAndStatusIsNot(EventStatus status);
}
