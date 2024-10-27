package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.*;
import main.server.dto.EventFullDto;
import main.server.mapper.EventMapper;
import main.server.repository.CategoryRepository;
import main.server.repository.EventRepository;
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
    @Autowired
    public EventService(EventRepository eventRepository,
                        UserRepository userRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

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
