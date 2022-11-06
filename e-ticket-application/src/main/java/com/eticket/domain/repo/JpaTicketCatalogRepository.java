package com.eticket.domain.repo;

import com.eticket.domain.entity.event.Event;
import com.eticket.domain.entity.event.TicketCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTicketCatalogRepository extends JpaRepository<TicketCatalog, Integer> {
    @Query(nativeQuery = true, value = "SELECT SUM(t.slot) FROM ticket_catalog t WHERE t.event_id = ?1 AND t.removed = false")
    int sumSlotByEventId(Integer eventId);

    @Query(nativeQuery = true, value = "SELECT SUM(t.soldSlot) FROM ticket_catalog t WHERE t.event_id = ?1 AND t.removed = false")
    int sumSoldSlotByEventId(Integer eventId);

    List<TicketCatalog> findByRemovedFalseAndEvent(Event event);

    Optional<TicketCatalog> findByIdAndRemovedFalse(Integer integer);
}
