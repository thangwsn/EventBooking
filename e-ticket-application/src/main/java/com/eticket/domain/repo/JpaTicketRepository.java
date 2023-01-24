package com.eticket.domain.repo;

import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.event.Event;
import com.eticket.domain.entity.event.Ticket;
import com.eticket.domain.entity.event.TicketCatalog;
import com.eticket.domain.entity.event.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaTicketRepository extends JpaRepository<Ticket, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM ticket WHERE ticket_catalog_id = ?1 AND status = ?2 ORDER BY id ASC LIMIT ?3")
    List<Ticket> getListTicketByTicketCatalog(int ticketCatalogId, String ticketStatus, int quantity);

    long countByTicketCatalogAndStatus(TicketCatalog ticketCatalog, TicketStatus status);

    List<Ticket> findAllByBooking(Booking booking);

    void deleteAllByEvent(Event event);

    @Query(nativeQuery = true, value = "SELECT * FROM ticket WHERE ticket_catalog_id = ?1 AND status = ?2 ORDER BY id DESC LIMIT ?3")
    List<Ticket> getListTicketByTicketCatalogDesc(int ticketCatalogId, String ticketStatus, int quantity);

    @Query(nativeQuery = true, value = "SELECT ticket.code FROM ticket WHERE ticket_catalog_id = ?1 ORDER BY id DESC LIMIT 1")
    String lastestTicketCodeByTicketCatalogId(Integer ticketCatalogId);

    List<Ticket> findAllByTicketCatalogId(Integer ticketCatalogId);
}
