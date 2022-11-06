package com.eticket.domain.repo;

import com.eticket.domain.entity.event.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaOrganizerRepository extends JpaRepository<Organizer, Integer> {
    Optional<Organizer> findByIdAndRemovedFalse(Integer organizerId);
    List<Organizer> findByRemovedFalse();
}
