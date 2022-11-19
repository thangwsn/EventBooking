package com.eticket.domain.service;

import com.eticket.application.api.dto.event.*;
import com.eticket.application.websocket.observer.MessageType;
import com.eticket.application.websocket.observer.Observable;
import com.eticket.domain.entity.account.Employee;
import com.eticket.domain.entity.account.User;
import com.eticket.domain.entity.booking.BookingStatus;
import com.eticket.domain.entity.event.*;
import com.eticket.domain.entity.quartz.ScheduleJob;
import com.eticket.domain.entity.quartz.ScheduleJobType;
import com.eticket.domain.repo.*;
import com.eticket.infrastructure.mapper.EventMap;
import com.eticket.infrastructure.quartz.model.ScheduleRequest;
import com.eticket.infrastructure.quartz.service.ScheduleService;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import com.eticket.infrastructure.utils.Constants;
import com.eticket.infrastructure.utils.Utils;
import org.apache.kafka.common.errors.AuthenticationException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends Observable<EventServiceImpl> implements EventService {
    @Autowired
    private JpaEventRepository eventRepository;
    @Autowired
    private JpaEmployeeRepository employeeRepository;
    @Autowired
    private JpaOrganizerRepository organizerRepository;
    @Autowired
    private JpaTicketRepository ticketRepository;
    @Autowired
    private JpaTicketCatalogRepository ticketCatalogRepository;
    @Autowired
    private JpaFollowRepository followRepository;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaImageRepository imageRepository;
    @Autowired
    private JpaBookingRepository bookingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EventMap eventMap;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private JpaScheduleJobRepository scheduleJobRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerEvent(MultipartFile[] files, String data) {
        EventCreateRequest eventCreateRequest = Utils.readObjectFromJsonFormat(data, EventCreateRequest.class);
        Event event = modelMapper.map(eventCreateRequest, Event.class);
        event.setRemoved(false);
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("Employee is not found!") {
            };
        }
        Employee employee = employeeRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new RuntimeException("Employee is not found!"));
        event.setUpdateBy(employee);
        event.setType(EventType.valueOf(eventCreateRequest.getTypeString()));
        event.setOrganizer(organizerRepository.findByIdAndRemovedFalse(eventCreateRequest.getOrganizerId()).get());
        event.setLocation(modelMapper.map(eventCreateRequest.getLocationDto(), Location.class));
        event.setSales(0.0);
        event.setStatus(EventStatus.CREATED);
        event.setRemoved(false);
        event.setSoldSlot(0);
        event.setRemainSlot(0);
        event.setStartTime(Utils.convertToTimeStamp(eventCreateRequest.getStartTimeMs()));
        event.setLaunchTime(Utils.convertToTimeStamp(eventCreateRequest.getLaunchTimeMs()));
        event.setCloseTime(Utils.convertToTimeStamp(eventCreateRequest.getCloseTimeMs()));
        event = eventRepository.saveAndFlush(event);
        // store files
        List<String> listImageURI = fileStorageService.storeListFile(files);
        List<Image> listImage = new ArrayList<>();
        for (String imageURI : listImageURI) {
            listImage.add(Image.builder().url(imageURI).event(event).build());
        }
        imageRepository.saveAll(listImage);
        scheduleEventJob(event);

    }

    @Override
    public void removeEvent(Integer eventId) {
        Event event = eventRepository.findByIdAndRemovedFalse(eventId)
                .orElseThrow(() -> new RuntimeException("Event is not found"));
        if (event.getStatus().equals(EventStatus.CREATED)) {
            event.setRemoved(true);
            eventRepository.saveAndFlush(event);
        } else {
            throw new RuntimeException("Can not remove");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addTicketCatalog(Integer eventId, TicketCatalogRequest ticketCatalogRequest) {
        int totalSlot = 0;
        Event event = eventRepository.findByIdAndRemovedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event is not found"));
        TicketCatalog ticketCatalog = modelMapper.map(ticketCatalogRequest, TicketCatalog.class);
        ticketCatalog.setSoldSlot(0);
        ticketCatalog.setRemainSlot(ticketCatalog.getSlot());
        ticketCatalog.setEvent(event);
        ticketCatalog.setRemoved(false);
        ticketCatalog = ticketCatalogRepository.saveAndFlush(ticketCatalog);
        generateAndSaveTicketList(ticketCatalog);
        totalSlot = ticketCatalogRepository.sumSlotByEventId(eventId);

        event.setTotalSlot(totalSlot);
        event.setRemainSlot(totalSlot - event.getSoldSlot());
        eventRepository.saveAndFlush(event);
    }

    @Override
    public ListEventGetResponse getAllEvent() {
        List<Event> events = eventRepository.findByRemovedFalseOrderByCreateTimeDesc();
        List<EventGetResponse> eventGetResponses = new ArrayList<>();
        for (Event event : events) {
            eventGetResponses.add(eventMap.toEventGetResponse(event, getFollowNum(event.getId()), false));
        }
        return new ListEventGetResponse(eventGetResponses.size(), eventGetResponses);
    }

    @Override
    public EventDetailGetResponse getEventById(Integer eventId) {
        Event event = eventRepository.findByIdAndRemovedFalse(eventId)
                .orElseThrow(() -> new RuntimeException("Event is not found"));
        return eventMap.toEventDetailGetResponse(event, getFollowNum(eventId), false);
    }

    @Override
    public EventDetailGetResponse getEventByIdFromClientSide(Integer eventId) {
        boolean isFollowed = false;
        boolean isDisableBooking = false;
        Event event = eventRepository.findByIdAndRemovedFalseAndStatusIsNot(eventId, EventStatus.CREATED)
                .orElseThrow(() -> new RuntimeException("Event is not found"));
        String username = jwtUtils.getUserNameFromJwtToken();
        Optional<User> userOptional = userRepository.findByUsernameAndRemovedFalse(username);
        if (userOptional.isPresent()) {
            isFollowed = followRepository.findByUserIdAndEventIdAndRemovedFalse(userOptional.get().getId(), eventId).isPresent();
            if (event.getType().equals(EventType.FREE)) {
                isDisableBooking = bookingRepository.findByRemovedFalseAndEventIdAndUserIdAndStatus(eventId, userOptional.get().getId(), BookingStatus.COMPLETED).isPresent();
            }
        }
        EventDetailGetResponse eventDetailGetResponse = eventMap.toEventDetailGetResponse(event, getFollowNum(eventId), true);
        eventDetailGetResponse.setFollowed(isFollowed);
        eventDetailGetResponse.setDisableBooking(isDisableBooking);
        return eventDetailGetResponse;
    }

    @Override
    public ListEventGetResponse getAllEventForUserSide(EventGetRequest request) {
        String sortField = request.getSortField();
        Sort sort = request.getSortDirection().equalsIgnoreCase(Constants.DESC_SORT) ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageSize(), sort);
        List<Event> events = eventRepository.findByRemovedFalseAndStatus(EventStatus.valueOf(request.getStatus()), pageable);
        List<EventGetResponse> eventGetResponses = new ArrayList<>();
        for (Event event : events) {
            eventGetResponses.add(eventMap.toEventGetResponse(event, getFollowNum(event.getId()), true));
        }
        return new ListEventGetResponse(eventGetResponses.size(), eventGetResponses);
    }

    @Override
    @Async
    @Transactional(rollbackFor = {Exception.class})
    public void toggleFollow(Integer eventId, String username) {
        Optional<Event> eventOption = eventRepository.findByIdAndRemovedFalse(eventId);
        if (!eventOption.isPresent()) {
            return;
        }
        Event event = eventOption.get();
        Optional<User> userOptional = userRepository.findByUsernameAndRemovedFalse(username);
        if (!userOptional.isPresent()) {
            return;
        }
        User user = userOptional.get();
        Optional<Follow> followOptional = followRepository.findByUserIdAndEventId(user.getId(), eventId);
        Follow follow = new Follow();
        if (followOptional.isPresent()) {
            follow = followOptional.get();
            follow.setRemoved(!follow.isRemoved());
            followRepository.saveAndFlush(follow);
        } else {
            follow.setUser(user);
            follow.setEvent(event);
            follow.setRemoved(false);
            follow = followRepository.saveAndFlush(follow);
        }
        if (!follow.isRemoved()) {
            notifyObservers(this, eventId, "", MessageType.FOLLOW);
        }
    }

    @Override
    public List<String> getListEventType() {
        return Arrays.asList(EventType.values()).stream().map((item) -> item.name()).collect(Collectors.toList());
    }

    @Override
    public List<String> getListEventStatus(Integer eventId) {
        List<String> listEventStatus = Arrays.asList(EventStatus.values()).stream().map((item) -> item.name()).collect(Collectors.toList());
        Event event = eventRepository.findByIdAndRemovedFalse(eventId).orElseThrow(() -> new ResourceNotFoundException("Event is not found"));
        String status = event.getStatus().name();
        if (status.equals("OPEN") || status.equals("CLOSE")) {
            listEventStatus.remove("CREATED");
        }
        if (status.equals("SOLD")) {
            listEventStatus.remove("CREATED");
            listEventStatus.remove("CLOSE");
        }
        if (status.equals("LIVE")) {
            listEventStatus.remove("CREATED");
            listEventStatus.remove("OPEN");
            listEventStatus.remove("CLOSE");
            listEventStatus.remove("SOLD");
        }
        if (status.equals("FINISH")) {
            listEventStatus.remove("CREATED");
            listEventStatus.remove("OPEN");
            listEventStatus.remove("CLOSE");
            listEventStatus.remove("SOLD");
            listEventStatus.remove("LIVE");
        }
        return listEventStatus;
    }

    @Override
    public void changeEventStatus(ChangeEventStatusRequest changeEventStatusRequest) {
        Event event = eventRepository.findByIdAndRemovedFalse(changeEventStatusRequest.getEventId()).orElseThrow(() -> new ResourceNotFoundException("Event is not found"));
        String eventStatus = event.getStatus().name();
        if (eventStatus.equals(changeEventStatusRequest.getTargetStatus())) {
            return;
        }
        if ((eventStatus.equals("OPEN") || eventStatus.equals("CLOSE")) && changeEventStatusRequest.getTargetStatus().equals("CREATED")) {
            return;
        }
        if (eventStatus.equals("SOLD") && (changeEventStatusRequest.getTargetStatus().equals("CREATED") || changeEventStatusRequest.getTargetStatus().equals("CLOSE"))) {
            return;
        }
        if (eventStatus.equals("LIVE") && !changeEventStatusRequest.getTargetStatus().equals("FINISH")) {
            return;
        }
        if (eventStatus.equals("FINISH")) {
            return;
        }
        event.setStatus(EventStatus.valueOf(changeEventStatusRequest.getTargetStatus()));
        eventRepository.saveAndFlush(event);
        // quartz schedule remove job
        cancelEventJob(event, changeEventStatusRequest.getTargetStatus());
    }

    @Override
    public List<EventWebSocketDto> getAllEventForWS() {
        List<Event> events = eventRepository.findByRemovedFalseAndStatusIsNot(EventStatus.FINISH);
        List<EventWebSocketDto> list = events.stream().map(e -> eventMap.toEventWebSocketDto(e, getFollowNum(e.getId()))).collect(Collectors.toList());
        return list;
    }

    private void generateAndSaveTicketList(TicketCatalog ticketCatalog) {
        String prefixCode = String.format("E%04d%04dN", ticketCatalog.getEvent().getId(), ticketCatalog.getId());
        List<Ticket> tickets = new ArrayList<>();
        for (int num = 1; num <= ticketCatalog.getSlot(); num++) {
            String code = prefixCode + String.format("%05d", num);
            Ticket ticket = Ticket.builder()
                    .code(code)
                    .price(ticketCatalog.getPrice())
                    .status(TicketStatus.AVAILABLE)
                    .event(ticketCatalog.getEvent())
                    .ticketCatalog(ticketCatalog)
                    .build();
            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
    }

    private int getFollowNum(Integer eventId) {
        Integer num = followRepository.countByEventIdAndRemovedFalse(eventId);
        return (num == null) ? 0 : num.intValue();
    }

    private void scheduleEventJob(Event event) {
        // open
        ScheduleJob eventOpenJob = ScheduleJob.builder().jobKey(Utils.generateUUID()).objectId(event.getId()).type(ScheduleJobType.EVENT_OPEN).removed(false).build();
        scheduleJobRepository.save(eventOpenJob);
        scheduleService.scheduleJob(new ScheduleRequest(eventOpenJob.getJobKey(), Constants.JOB_GROUP_EVENT, Constants.TRIGGER_EVENT, eventOpenJob.getType(), eventOpenJob.getObjectId(), event.getLaunchTime()));
        // close
        ScheduleJob eventCloseJob = ScheduleJob.builder().jobKey(Utils.generateUUID()).objectId(event.getId()).type(ScheduleJobType.EVENT_CLOSE).removed(false).build();
        scheduleJobRepository.save(eventCloseJob);
        scheduleService.scheduleJob(new ScheduleRequest(eventCloseJob.getJobKey(), Constants.JOB_GROUP_EVENT, Constants.TRIGGER_EVENT, eventCloseJob.getType(), eventCloseJob.getObjectId(), event.getCloseTime()));
        // live
        ScheduleJob eventLiveJob = ScheduleJob.builder().jobKey(Utils.generateUUID()).objectId(event.getId()).type(ScheduleJobType.EVENT_LIVE).removed(false).build();
        scheduleJobRepository.save(eventLiveJob);
        scheduleService.scheduleJob(new ScheduleRequest(eventLiveJob.getJobKey(), Constants.JOB_GROUP_EVENT, Constants.TRIGGER_EVENT, eventLiveJob.getType(), eventLiveJob.getObjectId(), event.getStartTime()));
        //finish
        ScheduleJob eventFinishJob = ScheduleJob.builder().jobKey(Utils.generateUUID()).objectId(event.getId()).type(ScheduleJobType.EVENT_FINISH).removed(false).build();
        scheduleJobRepository.save(eventFinishJob);
        scheduleService.scheduleJob(new ScheduleRequest(eventFinishJob.getJobKey(), Constants.JOB_GROUP_EVENT, Constants.TRIGGER_EVENT, eventFinishJob.getType(), eventFinishJob.getObjectId(), new Timestamp(event.getStartTime().getTime() + (long) event.getDuration() * 60 * 60 * 1000)));
    }

    private void cancelEventJob(Event event, String targetStatus) {
        Integer eventId = event.getId();
        if (targetStatus.equals(EventStatus.OPEN.name())) {
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_OPEN);
        }
        if (targetStatus.equals(EventStatus.SOLD.name())) {
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_OPEN);
        }
        if (targetStatus.equals(EventStatus.CLOSE.name())) {
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_OPEN);
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_CLOSE);
        }
        if (targetStatus.equals(EventStatus.LIVE.name())) {
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_OPEN);
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_CLOSE);
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_LIVE);
        }
        if (targetStatus.equals(EventStatus.FINISH.name())) {
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_OPEN);
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_CLOSE);
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_LIVE);
            cancelSingleEventJob(eventId, ScheduleJobType.EVENT_FINISH);
        }
    }

    private void cancelSingleEventJob(Integer eventId, String type) {
        Optional<ScheduleJob> scheduleJobOptional = scheduleJobRepository.findByRemovedFalseAndObjectIdAndType(eventId, type);
        if (!scheduleJobOptional.isPresent()) {
            return;
        }
        scheduleService.cancelJob(scheduleJobOptional.get().getJobKey(), Constants.JOB_GROUP_EVENT);
    }
}
