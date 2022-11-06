package com.eticket.domain.service;

import com.eticket.application.api.dto.booking.*;
import com.eticket.domain.entity.account.Account;
import com.eticket.domain.entity.account.Role;
import com.eticket.domain.entity.account.User;
import com.eticket.domain.entity.booking.*;
import com.eticket.domain.entity.event.*;
import com.eticket.domain.repo.*;
import com.eticket.infrastructure.kafka.producer.KafkaSendService;
import com.eticket.infrastructure.mapper.BookingMap;
import com.eticket.infrastructure.mapper.TicketMap;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import com.eticket.infrastructure.utils.Constants;
import com.eticket.infrastructure.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private KafkaSendService kafkaSendService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JpaEventRepository eventRepository;
    @Autowired
    private JpaBookingRepository bookingRepository;
    @Autowired
    private JpaTicketCatalogRepository ticketCatalogRepository;
    @Autowired
    private JpaTicketRepository ticketRepository;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaBookingDetailRepository bookingDetailRepository;
    @Autowired
    private JpaAccountRepository accountRepository;
    @Autowired
    private JpaPaymentRepository paymentRepository;
    @Autowired
    private BookingMap bookingMap;
    @Autowired
    private TicketMap ticketMap;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @KafkaListener(groupId = Constants.groupId, topics = Constants.BOOKING_HANDLER_TOPIC)
    public void handleBooking(String message) {
        // coding
        BookingCreateRequest bookingCreateRequest = Utils.readObjectFromJsonFormat(message, BookingCreateRequest.class);
        User user = userRepository.findByUsernameAndRemovedFalse(bookingCreateRequest.getUsername()).orElseThrow(() -> new RuntimeException("User is not found!"));
        Event event = eventRepository.findByIdAndRemovedFalse(bookingCreateRequest.getEventId()).orElseThrow(() -> new RuntimeException("Event is not found"));
        BookingCreateResponse bookingCreateResponse = new BookingCreateResponse();
        bookingCreateResponse.setBookingCode(bookingCreateRequest.getBookingCode());
        if (user.getRole().equals(Role.USER) && isAvailable(event, bookingCreateRequest.getListItem())) {
            Booking booking;
            if (event.getType().equals(EventType.FREE)) {
                booking = saveBooking(bookingCreateRequest, EventType.FREE, event, user);
                bookingCreateResponse.setMessage("Completed Booking");
            } else {
                booking = saveBooking(bookingCreateRequest, EventType.CHARGE, event, user);
                bookingCreateResponse.setMessage("Next step, payment");
            }
            bookingCreateResponse.setId(booking.getId());
            bookingCreateResponse.setBookingCode(booking.getCode());
            bookingCreateResponse.setEventType(event.getType());
            bookingCreateResponse.setPaymentType(PaymentType.valueOf(bookingCreateRequest.getPaymentType()));
            bookingCreateResponse.setEventTitle(event.getTitle());
            bookingCreateResponse.setFullName(booking.getFullName());
            bookingCreateResponse.setMobile(booking.getMobile());
            bookingCreateResponse.setAmount(booking.getAmount());
            bookingCreateResponse.setStatus(booking.getStatus());
            if (booking.getStatus().equals(BookingStatus.COMPLETED)) {
                List<String> listTicketCode = booking.getListTicket().stream().map(ticket -> ticket.getCode()).collect(Collectors.toList());
                bookingCreateResponse.setListTicketCode(listTicketCode);
            }
            bookingCreateResponse.setTicketNum(getBookingQuantity(bookingCreateRequest.getListItem()));
        } else {
            bookingCreateResponse.setStatus(BookingStatus.REJECT);
            bookingCreateResponse.setMessage("Some of item are full");
        }
        kafkaSendService.reactorSend(Constants.BOOKING_RESPONSE_TOPIC, bookingCreateResponse, bookingCreateResponse.getBookingCode());
        // if CHARGE Event ==> Quartz Schedule time out booking
    }

    @Transactional(rollbackFor = {Exception.class})
    Booking saveBooking(BookingCreateRequest bookingCreateRequest, EventType eventType, Event event, User user) {
        double amount = 0.0;
        Booking booking = new Booking();
        booking.setCode(bookingCreateRequest.getBookingCode());
        booking.setFullName(bookingCreateRequest.getFullName());
        booking.setMobile(bookingCreateRequest.getMobile());
        booking.setRemoved(false);
        booking.setUser(user);
        if (eventType.equals(EventType.FREE)) {
            booking.setStatus(BookingStatus.COMPLETED);
        } else if (eventType.equals(EventType.CHARGE)) {
            booking.setStatus(BookingStatus.PENDING);
        }
        List<Ticket> listTicket = new ArrayList<>();
        List<BookingDetail> bookingDetailList = new ArrayList<>();
        for (ItemCreateRequest ticketCatalog : bookingCreateRequest.getListItem()) {
            TicketCatalog ticketCatalog1 = ticketCatalogRepository.findByIdAndRemovedFalse(ticketCatalog.getTicketCatalogId()).orElseThrow(() -> new RuntimeException("Ticket Catalog is not found"));
            amount += ticketCatalog.getQuantity() * ticketCatalog1.getPrice();
            List<Ticket> list = ticketRepository.getListTicketByTicketCatalog(ticketCatalog.getTicketCatalogId(), TicketStatus.AVAILABLE.name(), ticketCatalog.getQuantity());
            listTicket.addAll(list);
            ticketCatalog1.setSoldSlot(ticketCatalog1.getSoldSlot() + ticketCatalog.getQuantity());
            ticketCatalog1.setRemainSlot(ticketCatalog1.getRemainSlot() - ticketCatalog.getQuantity());
            ticketCatalogRepository.saveAndFlush(ticketCatalog1);
        }
        booking.setAmount(amount);
        for (Ticket ticket : listTicket) {
            ticket.setBooking(booking);
            ticket.setUser(user);
            ticket.setStatus(TicketStatus.RESERVED);
            ticket.setSoldTime(Utils.convertToTimeStamp(new Date().getTime()));
        }
        booking.setEvent(event);
        booking = bookingRepository.save(booking);
        booking.setListTicket(listTicket);
        ticketRepository.saveAll(listTicket);

        for (ItemCreateRequest ticketCatalog : bookingCreateRequest.getListItem()) {
            TicketCatalog ticketCatalog1 = ticketCatalogRepository.findByIdAndRemovedFalse(ticketCatalog.getTicketCatalogId()).orElseThrow(() -> new RuntimeException("Ticket Catalog is not found"));
            bookingDetailList.add(BookingDetail.builder().booking(booking).quantity(ticketCatalog.getQuantity()).ticketCatalog(ticketCatalog1).build());
        }
        bookingDetailRepository.saveAll(bookingDetailList);

        int bookingQuantity = getBookingQuantity(bookingCreateRequest.getListItem());
        event.setSoldSlot(event.getSoldSlot() + bookingQuantity);
        event.setRemainSlot(event.getRemainSlot() - bookingQuantity);
        if (event.getRemainSlot() == 0) {
            event.setStatus(EventStatus.SOLD);
        }
        eventRepository.saveAndFlush(event);
        return booking;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBooking(Integer bookingId) {
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        User user = userRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new RuntimeException(String.format("Account not found by username %s ", usernameFromJwtToken)));
        if (!user.getRole().equals(Role.USER)) {
            throw new AuthenticationException("Unauthoraiztion") {
            };
        }
        Booking booking = bookingRepository.findByIdAndRemovedFalse(bookingId)
                .orElseThrow(() -> new RuntimeException(String.format("Booking %d not found ", bookingId)));
        List<Ticket> ticketList = booking.getListTicket();
        Event event = eventRepository.findByIdAndRemovedFalse(booking.getEvent().getId()).get();
        if (!(event.getStatus().equals(EventStatus.OPEN) || event.getStatus().equals(EventStatus.SOLD))) {
            return;
        }
        if (!user.getId().equals(booking.getUser().getId())) {
            return;
        }
        if ((booking.getStatus().equals(BookingStatus.COMPLETED) && event.getType().equals(EventType.FREE))
                || (booking.getStatus().equals(BookingStatus.PENDING))) {
            //update booking
            booking.setStatus(BookingStatus.CANCEL);
            //update ticket
            for (Ticket ticket : ticketList) {
                ticket.setBooking(null);
                ticket.setUser(null);
                ticket.setSoldTime(null);
                ticket.setQRcode(null);
                ticket.setStatus(TicketStatus.AVAILABLE);
            }
//            ticketRepository.saveAll(ticketList);
            booking.setListTicket(ticketList);
            //update ticket catalog
            List<BookingDetail> bookingDetailList = bookingDetailRepository.findAllByBooking(booking);
            List<TicketCatalog> ticketCatalogList = new ArrayList<>();
            int sum = 0;
            for (BookingDetail item : bookingDetailList) {
                TicketCatalog ticketCatalog = item.getTicketCatalog();
                ticketCatalog.setSoldSlot(ticketCatalog.getSoldSlot() - item.getQuantity());
                ticketCatalog.setRemainSlot(ticketCatalog.getRemainSlot() + item.getQuantity());
                ticketCatalogList.add(ticketCatalog);
                sum += item.getQuantity();
            }
            ticketCatalogRepository.saveAll(ticketCatalogList);
            // update event
            event.setSoldSlot(event.getSoldSlot() - sum);
            event.setRemainSlot(event.getRemainSlot() + sum);
            if (event.getStatus().equals(EventStatus.SOLD) && event.getRemainSlot() > 0) {
                event.setStatus(EventStatus.OPEN);
            }
            booking.setEvent(event);
//            eventRepository.save(event);
            bookingRepository.save(booking);
            ticketRepository.saveAll(ticketList);
        }
    }

    @Override
    public ListBookingGetResponse getListBooking(BookingGetRequest request) {
        String sortField = request.getSortField();
        Sort sort = request.getSortDirection().equalsIgnoreCase(Constants.DESC_SORT) ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(request.getPageNo() - 1, request.getPageSize(), sort);
        Account account = accountRepository.findByUsernameAndRemovedFalse(jwtUtils.getUserNameFromJwtToken()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        boolean userView = account.getRole().equals(Role.USER);
        List<Booking> bookingList = new ArrayList<>();
        if (userView) {
            User user = userRepository.findByUsernameAndRemovedFalse(account.getUsername()).get();
            if (request.getStatus().equals("")) {
                bookingList = bookingRepository.findByRemovedFalseAndUser(user, pageable);
            } else {
                bookingList = bookingRepository.findByRemovedFalseAndUserAndStatus(user, request.getStatus(), pageable);
            }
        } else {
            bookingList = bookingRepository.findByRemovedFalse(pageable);
        }
        List<BookingGetResponse> bookingGetResponseList = bookingList.stream()
                .map((i) -> {
                    List<BookingDetail> ticketCalalogItem = bookingDetailRepository.findAllByBooking(i);
                    List<BookingCatalogItem> bookingCatalogItems = ticketCalalogItem.stream().map(item -> new BookingCatalogItem(item.getTicketCatalog().getId(), item.getTicketCatalog().getTitle(), item.getTicketCatalog().getPrice(), item.getQuantity())).collect(Collectors.toList());
                    return bookingMap.toBookingGet(i, bookingCatalogItems);
                })
                .collect(Collectors.toList());
        return new ListBookingGetResponse(bookingGetResponseList.size(), bookingGetResponseList);
    }

    @Override
    public BookingDetailResponse getBookingDetail(Integer bookingId) {
        Account account = accountRepository.findByUsernameAndRemovedFalse(jwtUtils.getUserNameFromJwtToken()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        boolean userView = account.getRole().equals(Role.USER);
        Booking booking;
        if (userView) {
            User user = userRepository.findByUsernameAndRemovedFalse(account.getUsername()).get();
            booking = bookingRepository.findByIdAndUserAndRemovedFalse(bookingId, user).orElseThrow(() -> new RuntimeException(""));
        } else {
            booking = bookingRepository.findByIdAndRemovedFalse(bookingId).orElseThrow(() -> new RuntimeException(""));
        }
        List<BookingDetail> ticketCalalogItem = bookingDetailRepository.findAllByBooking(booking);
        List<BookingCatalogItem> bookingCatalogItems = ticketCalalogItem.stream().map(item -> new BookingCatalogItem(item.getTicketCatalog().getId(), item.getTicketCatalog().getTitle(), item.getTicketCatalog().getPrice(), item.getQuantity())).collect(Collectors.toList());
        List<TicketGetResponse> ticketList = new ArrayList<>();
        if (userView) {
            ticketList = ticketRepository.findAllByBooking(booking).stream().map((i) -> ticketMap.toTicketGet(i)).collect(Collectors.toList());
        }
        PaymentGetResponse paymentGetResponse = new PaymentGetResponse();
        Optional<Payment> paymentOptional = paymentRepository.findByBooking(booking);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            paymentGetResponse = modelMapper.map(payment, PaymentGetResponse.class);
            paymentGetResponse.setCreateTime(payment.getCreateTime().getTime());
            paymentGetResponse.setTypeString(payment.getType().toString());
        }
        BookingDetailResponse bookingDetailResponse = bookingMap.toBookingDetail(booking, bookingCatalogItems, ticketList, paymentGetResponse, userView);
        return bookingDetailResponse;
    }

    private int getSoldSlotOfTicketCatalog(TicketCatalog ticketCatalog) {
        int soldSlot = (int) ticketRepository.countByTicketCatalogAndStatus(ticketCatalog, TicketStatus.RESERVED);
        return soldSlot;
    }

    void updateTicketCatalog(Integer ticketCatalogId) {
        Optional<TicketCatalog> ticketCatalogOptional = ticketCatalogRepository.findByIdAndRemovedFalse(ticketCatalogId);
        if (ticketCatalogOptional.isPresent()) {
            TicketCatalog ticketCatalog = ticketCatalogOptional.get();
            ticketCatalog.setSoldSlot(getSoldSlotOfTicketCatalog(ticketCatalog));
            ticketCatalog.setRemainSlot(ticketCatalog.getSlot() - ticketCatalog.getSoldSlot());
            ticketCatalogRepository.save(ticketCatalog);
        }
    }

    void updateEventSlot(Integer eventId) {
        Optional<Event> eventOptional = eventRepository.findByIdAndRemovedFalse(eventId);
        if (!eventOptional.isPresent()) {
            return;
        }
        Event event = eventOptional.get();
        int soldSlot = ticketCatalogRepository.sumSoldSlotByEventId(eventId);
        event.setSoldSlot(soldSlot);
        event.setRemainSlot(event.getTotalSlot() - soldSlot);
        if (event.getStatus().equals(EventStatus.SOLD) && event.getRemainSlot() > 0) {
            event.setStatus(EventStatus.OPEN);
        }
        eventRepository.save(event);
    }

    private boolean isAvailable(Event event, List<ItemCreateRequest> listItem) {
        List<TicketCatalog> listTicketCatalog = ticketCatalogRepository.findByRemovedFalseAndEvent(event);
        if (listTicketCatalog == null || listTicketCatalog.size() == 0) {
            return false;
        }
        for (TicketCatalog ticketCatalog : listTicketCatalog) {
            for (ItemCreateRequest itemCreateRequest : listItem) {
                if (ticketCatalog.getId().intValue() == itemCreateRequest.getTicketCatalogId() && ticketCatalog.getRemainSlot() < itemCreateRequest.getQuantity()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getBookingQuantity(List<ItemCreateRequest> listItem) {
        int sum = 0;
        for (ItemCreateRequest itemCreateRequest : listItem) {
            sum += itemCreateRequest.getQuantity();
        }
        return sum;
    }
}
