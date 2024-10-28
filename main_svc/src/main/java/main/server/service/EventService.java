package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.*;
import main.server.dto.*;
import main.server.mapper.EventMapper;
import main.server.mapper.RequestMapper;
import main.server.repository.CategoryRepository;
import main.server.repository.EventRepository;
import main.server.repository.RequestRepository;
import main.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository  requestRepository;
    @Autowired
    public EventService(EventRepository eventRepository,
                        UserRepository userRepository, CategoryRepository categoryRepository, RequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
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
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(userRepository.findById(userId).orElseThrow());
        event.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElseThrow());
        event.setState("PENDING");
        Event eventUp = eventRepository.save(event);
        log.info("createdEvent: {}", eventUp);
        return EventMapper.toEventFullDto(eventUp);
    }
    public EventFullDto getEventPriv(int userId, int eventId) {
        log.info("getEvent: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow();
        log.info("gotEvent: {}", event);
        return EventMapper.toEventFullDto(event);
    }
    public EventFullDto updateEventPriv(int userId, int eventId, UpdateEventUserRequest  updateEventUserRequest) {
        log.info("updateEvent: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow();
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
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocationLat(updateEventUserRequest.getLocation().getLat());
            event.setLocationLng(updateEventUserRequest.getLocation().getLng());
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
            event.setState(updateEventUserRequest.getStateAction());
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
        List<Request> requests = requestRepository.findAllByEventId(eventId).stream().toList();
        log.info("gotEventRequests: {}", requests);
        return requests.stream().map(RequestMapper::toDto).toList();
    }
    public EventRequestStatusUpdateResult updateEventRequestsPriv(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest){
        log.info("updateEventRequests: {}", eventRequestStatusUpdateRequest);
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
            throw new RuntimeException("Event is full");
        }
        List<Request> requests = requestRepository.findAllById(reqIds);
        if (requests.stream().anyMatch(request -> !request.getStatus().equals("PENDING"))) {
            throw new RuntimeException("Request status is not PENDING");
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
            throw new RuntimeException("Request status is not PENDING");
        }
        for (Request request : requests) {
            request.setStatus("REJECTED");
        }
        List<Request> requestsUp = requestRepository.saveAll(requests);
        log.info("rejectedEventRequest: {}", requestsUp);
        return requestsUp.stream().map(RequestMapper::toDto).toList();
    }
    /*public List<EventFullDto> getEvents(String text, List<Integer> categories, Boolean paid,
                                        String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                        String sort, int from, int size) {
        log.info("getEvents");
        List<Event> events = eventRepository.findAll().stream()
                .filter(event -> text == null || event.getAnnotation().contains(text) || event.getDescription().contains(text))
                .filter(event -> categories == null || categories.isEmpty() || categories.contains(event.getCategory().getId()))
                .filter(event -> paid == null || event.getPaid() == paid)
                .filter(event -> rangeStart == null || event.getEventDate().isAfter(rangeStart))
                .filter(event -> rangeEnd == null || event.getEventDate().isBefore(rangeEnd))
                .filter(event -> !onlyAvailable || event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests())
                .skip(from)
                .limit(size)
                .toList();
        return events.stream().map(EventMapper::toEventFullDto).toList();
    }*/

    public List<EventFullDto> getEventsAdmin(List<Integer> usersIds, List<String> states,
                                        List<Integer> categories, String rangeStart,
                                        String rangeEnd, int from, int size) {
        log.info("getEvents");
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
                .filter(event -> states == null || states.isEmpty() || states.contains(event.getState().toString()))
                .filter(event -> finalCategories.contains(event.getCategory().getId()))
                .skip(from)
                .limit(size)
                .toList();
        return events.stream().map(EventMapper::toEventFullDto).toList();
    }

    public EventFullDto updateEventAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("updateEventAdmin: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = updateEventAdminRequest.getLocation();
            event.setLocationLat(location.getLat());
            event.setLocationLng(location.getLng());
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
            event.setState(updateEventAdminRequest.getStateAction());
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
