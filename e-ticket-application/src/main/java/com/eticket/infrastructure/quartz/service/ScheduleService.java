package com.eticket.infrastructure.quartz.service;

import com.eticket.domain.entity.quartz.ScheduleJob;
import com.eticket.domain.repo.JpaScheduleJobRepository;
import com.eticket.infrastructure.quartz.job.ScheduleJobHandler;
import com.eticket.infrastructure.quartz.model.ScheduleRequest;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private JpaScheduleJobRepository scheduleJobRepository;

    public void scheduleJob(ScheduleRequest request) {
        if (request.getStartAt().before(new Timestamp(new Date().getTime()))) {
            return;
        }
        JobDetail jobDetail = buildJobDetail(request);
        Trigger trigger = buildJobTrigger(jobDetail, request.getTriggeGroup(), request.getStartAt());
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelJob(String jobKey, String jobGroup) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobKey, jobGroup));
            if (jobDetail == null) {
                return;
            }
            scheduler.deleteJob(jobDetail.getKey());
            ScheduleJob scheduleJob = scheduleJobRepository.findByRemovedFalseAndJobKey(jobKey).get();
            scheduleJob.setRemoved(true);
            scheduleJobRepository.save(scheduleJob);
            logger.info("Delete job has key {} at {}", jobKey, new Date());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }

    private JobDetail buildJobDetail(ScheduleRequest request) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("type", request.getType());
        jobDataMap.put("objectId", request.getObjectId().toString());
        return JobBuilder.newJob(ScheduleJobHandler.class)
                .withIdentity(request.getJobKey(), request.getJobGroup())
                .withDescription(request.getType())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, String triggerGroup, Timestamp startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), triggerGroup)
                .withDescription("Schedule " + triggerGroup)
                .startAt(new Date(startAt.getTime()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
