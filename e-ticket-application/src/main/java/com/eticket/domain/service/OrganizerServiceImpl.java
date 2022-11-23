package com.eticket.domain.service;

import com.eticket.application.api.dto.event.*;
import com.eticket.domain.entity.account.Employee;
import com.eticket.domain.entity.event.Organizer;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.repo.JpaEmployeeRepository;
import com.eticket.domain.repo.JpaEventRepository;
import com.eticket.domain.repo.JpaFollowRepository;
import com.eticket.domain.repo.JpaOrganizerRepository;
import com.eticket.infrastructure.mapper.EventMap;
import com.eticket.infrastructure.mapper.OrganizerMap;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizerServiceImpl implements OrganizerService {
    @Autowired
    private JpaOrganizerRepository organizerRepository;
    @Autowired
    private JpaEmployeeRepository employeeRepository;
    @Autowired
    private JpaEventRepository eventRepository;
    @Autowired
    private JpaFollowRepository followRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventMap eventMap;
    @Autowired
    private OrganizerMap organizerMap;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean registerOrganizer(OrganizerCreateRequest organizerCreateRequest) throws ResourceNotFoundException {
        Organizer organizer = modelMapper.map(organizerCreateRequest, Organizer.class);
        organizer.setRemoved(false);
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("401 Unauthorized") {
            };
        }
        Employee employee = employeeRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found employee by username is %s ", usernameFromJwtToken)));
        organizer.setUpdateByEmployee(employee);
        organizerRepository.saveAndFlush(organizer);
        return true;
    }

    @Override
    public void removeOrganizer(Integer organizerId) {
        Organizer organizer = organizerRepository.findByIdAndRemovedFalse(organizerId).get();
        organizer.setRemoved(true);
        organizerRepository.saveAndFlush(organizer);
    }

    @Override
    public ListOrganizerGetResponse getAllOrganizer() {
        List<Organizer> organizerEntityList = organizerRepository.findByRemovedFalse();
        if (organizerEntityList.size() == 0) {
            return new ListOrganizerGetResponse(0, new ArrayList<OrganizerGetResponse>());
        }
        List<OrganizerGetResponse> organizerList = organizerEntityList
                .stream()
                .map(organizer -> organizerMap.toOrganizerGetResponse(organizer))
                .collect(Collectors.toList());
        return new ListOrganizerGetResponse(organizerList.size(), organizerList);
    }

    @Override
    public OrganizerGetDetailResponse getOrganizerById(Integer organizerId) {
        Organizer organizer = organizerRepository.findByIdAndRemovedFalse(organizerId).get();
        OrganizerGetDetailResponse response = modelMapper.map(organizer, OrganizerGetDetailResponse.class);
        response.setUpdateBy(organizer.getUpdateByEmployee().getUsername());
        List<EventGetResponse> listEvent = eventRepository.findAllByOrganizerIdAndRemovedFalse(organizerId)
                .stream()
                .map(event -> eventMap.toEventGetResponse(event, getFollowNum(event.getId()), false))
                .collect(Collectors.toList());
        response.setListEvent(listEvent);
        return response;
    }

    private int getFollowNum(Integer eventId) {
        Integer num = followRepository.countByEventIdAndRemovedFalse(eventId);
        return (num == null) ? 0 : num.intValue();
    }
}
