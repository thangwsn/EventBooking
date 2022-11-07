package com.eticket.infrastructure.quartz.job;

import com.eticket.application.api.dto.event.ChangeEventStatusRequest;
import com.eticket.domain.entity.event.EventStatus;
import com.eticket.domain.entity.quartz.ScheduleJobType;
import com.eticket.domain.service.BookingService;
import com.eticket.domain.service.EventService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Executing Job with key {}", context.getJobDetail().getKey());
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String type = jobDataMap.getString("type");
        Integer objectId = Integer.parseInt(jobDataMap.getString("objectId"));
        switch (type) {
            case ScheduleJobType.BOOKING_TIMEOUT:
                scheduleBookingTimeout(objectId);
                break;
            case ScheduleJobType.EVENT_OPEN:
                scheduleEventOpen(objectId);
                break;
            case ScheduleJobType.EVENT_CLOSE:
                scheduleEventClose(objectId);
                break;
            case ScheduleJobType.EVENT_LIVE:
                scheduleEventLive(objectId);
                break;
            case ScheduleJobType.EVENT_FINISH:
                scheduleEventFinish(objectId);
                break;
        }
    }

    private void scheduleBookingTimeout(Integer bookingId) {
        logger.info("Handle Booking Timeout with booking id = {}", bookingId);
        bookingService.cancelBooking(bookingId, false);
    }

    private void scheduleEventOpen(Integer eventId) {
        logger.info("Handle change to open status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.OPEN.name()));
    }

    private void scheduleEventClose(Integer eventId) {
        logger.info("Handle change to close status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.CLOSE.name()));
    }

    private void scheduleEventLive(Integer eventId) {
        logger.info("Handle change to live status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.LIVE.name()));
    }

    private void scheduleEventFinish(Integer eventId) {
        logger.info("Handle change to finish status with event od = {}", eventId);
        eventService.changeEventStatus(new ChangeEventStatusRequest(eventId, EventStatus.FINISH.name()));
    }
}