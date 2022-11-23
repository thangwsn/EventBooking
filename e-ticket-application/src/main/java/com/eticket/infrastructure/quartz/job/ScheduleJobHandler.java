package com.eticket.infrastructure.quartz.job;

import com.eticket.application.api.dto.event.ChangeEventStatusRequest;
import com.eticket.domain.entity.event.EventStatus;
import com.eticket.domain.entity.quartz.ScheduleJobType;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.AuthorizationException;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.service.BookingService;
import com.eticket.domain.service.EventService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ScheduleJobHandler extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobHandler.class);
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        logger.info("Executing Job with key {}", context.getJobDetail().getKey());
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String type = jobDataMap.getString("type");
        Integer objectId = Integer.parseInt(jobDataMap.getString("objectId"));
        switch (type) {
            case ScheduleJobType.BOOKING_TIMEOUT:
                try {
                    scheduleBookingTimeout(objectId);
                } catch (AuthenticationException e) {
                    throw new RuntimeException(e);
                } catch (AuthorizationException e) {
                    throw new RuntimeException(e);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ScheduleJobType.EVENT_OPEN:
                try {
                    scheduleEventOpen(objectId);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ScheduleJobType.EVENT_CLOSE:
                try {
                    scheduleEventClose(objectId);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ScheduleJobType.EVENT_LIVE:
                try {
                    scheduleEventLive(objectId);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ScheduleJobType.EVENT_FINISH:
                try {
                    scheduleEventFinish(objectId);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private void scheduleBookingTimeout(Integer bookingId) throws AuthenticationException, AuthorizationException, ResourceNotFoundException {
        logger.info("Handle Booking Timeout with booking id = {}", bookingId);
        bookingService.cancelBooking(bookingId, false);
    }

    private void scheduleEventOpen(Integer eventId) throws ResourceNotFoundException {
        logger.info("Handle change to open status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.OPEN.name()));
    }

    private void scheduleEventClose(Integer eventId) throws ResourceNotFoundException {
        logger.info("Handle change to close status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.CLOSE.name()));
    }

    private void scheduleEventLive(Integer eventId) throws ResourceNotFoundException {
        logger.info("Handle change to live status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.LIVE.name()));
    }

    private void scheduleEventFinish(Integer eventId) throws ResourceNotFoundException {
        logger.info("Handle change to finish status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.FINISH.name()));
    }
}
