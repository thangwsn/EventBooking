package com.eticket.domain.dao;

import com.eticket.domain.entity.event.Event;
import com.eticket.domain.entity.event.EventStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository(value = "eventDAO")
@Transactional(rollbackFor = Exception.class)
public class EventDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Event> getPopularEvent() {
        List<Event> eventList = entityManager.createQuery("SELECT e FROM Event e WHERE e.removed = false AND e.status = :eventStatus ORDER BY SUM(e.soldSlot + 1) DESC" +
                        "")
                .setParameter("eventStatus", EventStatus.OPEN.name())
                .setMaxResults(4)
                .getResultList();
        return eventList;
    }
}
