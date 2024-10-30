package main.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ViewStats;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import main.server.dao.*;
import main.server.dto.*;
import main.server.exception.ConflictException;
import main.server.exception.NotFoundException;
import main.server.mapper.EventMapper;
import main.server.mapper.RequestMapper;
import main.server.repository.CategoryRepository;
import main.server.repository.EventRepository;
import main.server.repository.RequestRepository;
import main.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import stat.client.StatClient;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository  requestRepository;
    private final StatClient statClient;
    @Autowired
    public EventService(EventRepository eventRepository,
                        UserRepository userRepository, CategoryRepository categoryRepository, RequestRepository requestRepository, StatClient statClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.statClient = statClient;
    }
    public EventFullDto  getEventByIdPub(int eventId, HttpServletRequest request) {
        log.info("getEvent: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow( () -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getState().equals("PUBLISHED")) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        log.info("gotEvent: {}", event);
        ResponseEntity<Object> stats = statClient.stats(event.getPublishedOn().format(Event.DATE_TIME_FORMATTER), LocalDateTime.now().format(Event.DATE_TIME_FORMATTER), new String[]{"/events/" + eventId}, true);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStats> viewStats = objectMapper.convertValue(stats.getBody(), new TypeReference<List<ViewStats>>() {});
        if (viewStats == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        event.setViews(viewStats.getFirst().getHits());
        eventRepository.save(event);
        statClient.hit("ewm-main-service", "/events/" + eventId, request.getRemoteAddr(),LocalDateTime.now().format(Event.DATE_TIME_FORMATTER));
        return EventMapper.toEventFullDto(event);
    }
    public List<EventShortDto> getEventsPriv(int userId, int from, int size){
        log.info("getEvents");
        List<Event> events = eventRepository.findAllByInitiatorId(userId).stream()
                .skip(from)
                .limit(size)
                .toList();
        log.info("gotEvents: {}", events);
        return events.stream().map(EventMapper::toEventShortDto).toList();
    }
    public EventFullDto createEventPriv(int userId, NewEventDto newEventDto) {
        log.info("createEvent: {}", newEventDto);
        if (LocalDateTime.parse(newEventDto.getEventDate(), Event.DATE_TIME_FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Event date must be at least 2 hours from now");
        }
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(userRepository.findById(userId).orElseThrow());
        event.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElseThrow());
        event.setState("PENDING");
        try {
            Event eventUp = eventRepository.save(event);
            log.info("createdEvent: {}", eventUp);
            return EventMapper.toEventFullDto(eventUp);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }
    public EventFullDto getEventPriv(int userId, int eventId) {
        log.info("getEvent: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        log.info("gotEvent: {}", event);
        return EventMapper.toEventFullDto(event);
    }
    public EventFullDto updateEventPriv(int userId, int eventId, UpdateEventUserRequest  updateEventUserRequest) {
        log.info("updateEvent: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow( () -> new NotFoundException("Event with id=" + eventId + " was not found") );
        if (!event.getState().equals("PENDING") && !event.getState().equals("CANCELED")) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow());
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate(), Event.DATE_TIME_FORMATTER));
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocationLat(updateEventUserRequest.getLocation().getLat());
            event.setLocationLng(updateEventUserRequest.getLocation().getLon());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState("PENDING");
            } else if (updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState("CANCELED");
            }
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        Event eventUp = eventRepository.save(event);
        log.info("updatedEvent: {}", eventUp);
        return EventMapper.toEventFullDto(eventUp);
    }
    public List<ParticipationRequestDto> getEventRequestsPriv(int userId, int eventId){
        log.info("getEventRequests: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow( () -> new NotFoundException("Event with id=" + eventId + " was not found") );
        List<Request> requests = requestRepository.findAllByEventId(eventId).stream().toList();
        log.info("gotEventRequests: {}", requests);
        return requests.stream().map(RequestMapper::toDto).toList();
    }
    public EventRequestStatusUpdateResult updateEventRequestsPriv(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest){
        log.info("updateEventRequests: {}", eventRequestStatusUpdateRequest);
        Event event = eventRepository.findById(eventId).orElseThrow( () -> new NotFoundException("Event with id=" + eventId + " was not found") );
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> participationRequestDtos = null;
        if (eventRequestStatusUpdateRequest.getStatus().equals("CONFIRMED")) {
            participationRequestDtos = confirmEventRequestsPriv(userId, eventId, eventRequestStatusUpdateRequest);
        } else if (eventRequestStatusUpdateRequest.getStatus().equals("REJECTED")) {
            participationRequestDtos = rejectEventRequestsPriv(userId, eventId, eventRequestStatusUpdateRequest);
        }
        if  (participationRequestDtos == null) {
            throw new RuntimeException("Request status is not CONFIRMED or REJECTED");
        }
        eventRequestStatusUpdateResult.setConfirmedRequests(participationRequestDtos.stream().filter(request -> request.getStatus().equals("CONFIRMED")).toList());
        eventRequestStatusUpdateResult.setRejectedRequests(participationRequestDtos.stream().filter(request -> request.getStatus().equals("REJECTED")).toList());
        log.info("updatedEventRequests: {}", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }
    private List<ParticipationRequestDto> confirmEventRequestsPriv(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest){
        List<Integer> reqIds = eventRequestStatusUpdateRequest.getRequestIds();
        String status = eventRequestStatusUpdateRequest.getStatus();
        log.info("confirmEventRequests: {}", reqIds);
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ConflictException("Participant limit has been reached");
        }
        List<Request> requests = requestRepository.findAllById(reqIds);
        if (requests.stream().anyMatch(request -> !request.getStatus().equals("PENDING"))) {
            throw new ConflictException("Request status is not PENDING");
        }
        int requestsLeft = event.getParticipantLimit() - event.getConfirmedRequests();
        for (Request request : requests) {
            if (requestsLeft > 0) {
                request.setStatus(status);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                requestsLeft--;
            } else {
                request.setStatus("REJECTED");
            }
        }
        List<Request> requestsUp = requestRepository.saveAll(requests);
        Event eventUp = eventRepository.save(event);
        log.info("confirmedEventRequest: {}", requestsUp);
        return requestsUp.stream().map(RequestMapper::toDto).toList();
    }
    private List<ParticipationRequestDto> rejectEventRequestsPriv(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest){
        List<Integer> reqIds = eventRequestStatusUpdateRequest.getRequestIds();
        log.info("rejectEventRequests: {}", reqIds);
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<Request> requests = requestRepository.findAllById(reqIds);
        if (requests.stream().anyMatch(request -> !request.getStatus().equals("PENDING"))) {
            throw new ConflictException("Request status is not PENDING");
        }
        for (Request request : requests) {
            request.setStatus("REJECTED");
        }
        List<Request> requestsUp = requestRepository.saveAll(requests);
        log.info("rejectedEventRequest: {}", requestsUp);
        return requestsUp.stream().map(RequestMapper::toDto).toList();
    }
    public List<EventShortDto> getEventsPub(String text, List<Integer> categories, Boolean paid,
                                        String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                        String sort, int from, int size, HttpServletRequest request) {
        log.info("getEvents: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        List<Event> events = eventRepository.findAll().stream()
                .filter(event -> event.getState().equals("PUBLISHED"))
                .filter(event -> text == null || text.equals("0") || event.getAnnotation().toLowerCase().contains(text.toLowerCase()) || event.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(event -> categories == null || categories.isEmpty() || categories.get(0) == 0 || categories.contains(event.getCategory().getId()))
                .filter(event -> paid == null || event.isPaid() == paid)
                .filter(event -> ((rangeStart == null && event.getEventDate().isAfter(LocalDateTime.now())) || event.getEventDate().isAfter(LocalDateTime.parse(rangeStart, Event.DATE_TIME_FORMATTER))))
                .filter(event -> rangeEnd == null || event.getEventDate().isBefore(LocalDateTime.parse(rangeEnd, Event.DATE_TIME_FORMATTER)))
                .filter(event -> onlyAvailable == null || onlyAvailable.equals(false) || event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests())
                .skip(from)
                .limit(size)
                .sorted((event1, event2) -> {
                    if (sort == null || sort.equals("0")) {
                        return 0;
                    }
                    return switch (sort) {
                        case "EVENT_DATE" -> event1.getEventDate().compareTo(event2.getEventDate());
                        case "VIEWS" -> event1.getViews() - event2.getViews();
                        default -> 0;
                    };
                })
                .toList();
        log.info("gotEvents: {}", events);
        statClient.hit("ewm-main-service", "/events", request.getRemoteAddr(),LocalDateTime.now().format(Event.DATE_TIME_FORMATTER));
        return events.stream().map(EventMapper::toEventShortDto).toList();
    }

    public List<EventFullDto> getEventsAdmin(List<Integer> usersIds, List<String> states,
                                        List<Integer> categories, String rangeStart,
                                        String rangeEnd, int from, int size) {
        log.info("getEventsAdmin: usersIds={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                usersIds, states, categories, rangeStart, rangeEnd, from, size);
        if  (usersIds == null || usersIds.isEmpty() || usersIds.get(0) == 0) {
            usersIds = userRepository.findAll().stream().map(User::getId).toList();
        }
        if (categories == null || categories.isEmpty() || categories.get(0) == 0) {
            categories = categoryRepository.findAll().stream().map(Category::getId).toList();
        }
        List<Integer> finalUsersIds = usersIds;
        List<Integer> finalCategories = categories;
        List<Event> events = eventRepository.findAll().stream()
                .filter(event -> finalUsersIds.contains(event.getInitiator().getId()))
                .filter(event -> states == null || states.isEmpty() || states.contains(event.getState()))
                .filter(event -> finalCategories.contains(event.getCategory().getId()))
                .filter(event -> rangeStart == null || event.getEventDate().isAfter(LocalDateTime.parse(rangeStart, Event.DATE_TIME_FORMATTER)))
                .filter(event -> rangeEnd == null || event.getEventDate().isBefore(LocalDateTime.parse(rangeEnd, Event.DATE_TIME_FORMATTER)))
                .skip(from)
                .limit(size)
                .toList();
        return events.stream().map(EventMapper::toEventFullDto).toList();
    }

    public EventFullDto updateEventAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("updateEventAdmin: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow( () -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = updateEventAdminRequest.getLocation();
            event.setLocationLat(location.getLat());
            event.setLocationLng(location.getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                if (event.getState().equals("PUBLISHED") || event.getState().equals("CANCELED")) {
                    throw new ConflictException("Event is already published or canceled");
                }
                event.setState("PUBLISHED");
            } else if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                if (event.getState().equals("PUBLISHED")) {
                    throw new ConflictException("Event is already published");
                }
                event.setState("CANCELED");
            }
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventAdminRequest.getEventDate(), Event.DATE_TIME_FORMATTER).isBefore(event.getEventDate().plusHours(1))){
                event.setEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), Event.DATE_TIME_FORMATTER));
            } else {
                throw new ConflictException("Event date must be at least 1 hour after publication");
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        Event eventUp = eventRepository.save(event);
        log.info("eventUpdated: {}", eventUp);
        return EventMapper.toEventFullDto(eventUp);
    }

    public void deleteEvent(int eventId) {
        log.info("deleteEvent: {}", eventId);
        eventRepository.deleteById(eventId);
    }
}
