package main.server.controller.priv;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.EventRequestStatusUpdateResult;
import main.server.dto.*;
import main.server.exception.ConflictException;
import main.server.service.EventService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class PrivEventController {
    private final EventService eventService;

    public PrivEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable int userId, @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("getUserEvents: userId={}, from={}, size={}", userId, from, size);
        List<EventShortDto> events = eventService.getEventsPriv(userId, from, size);
        log.info("getUserEvents: events={}", events);
        return events;
    }

    @PostMapping("{userId}/events")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public EventFullDto addUserEvent(@PathVariable int userId, @RequestBody NewEventDto newEventDto) {
        log.info("addUserEvent: userId={}, newEventDto={}", userId, newEventDto);
        newEventDto.validate();
        EventFullDto  eventFullDto = eventService.createEventPriv(userId, newEventDto);
        log.info("addUserEvent: eventFullDto={}", eventFullDto);
        return eventFullDto;
    }


    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable int userId, @PathVariable int eventId) {
        log.info("getUserEventById: userId={}, eventId={}", userId, eventId);
        EventFullDto eventFullDto = eventService.getEventPriv(userId, eventId);
        log.info("getUserEventById: eventFullDto={}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEventById(@PathVariable int userId, @PathVariable int eventId,
                                            @RequestBody UpdateEventUserRequest newEventDto) {
        log.info("updateUserEventById: userId={}, eventId={}, newEventDto={}", userId, eventId, newEventDto);
        if (Objects.equals(eventService.getEventPriv(userId, eventId).getState(), "PUBLISHED")) {
            throw new ConflictException("Event is published");
        }
        newEventDto.validate();
        EventFullDto eventFullDto = eventService.updateEventPriv(userId, eventId, newEventDto);
        log.info("updateUserEventById: eventFullDto={}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable int userId, @PathVariable int eventId) {
        log.info("getUserEventRequests: userId={}, eventId={}", userId, eventId);
        List<ParticipationRequestDto> participationRequestDtos = eventService.getEventRequestsPriv(userId, eventId);
        log.info("getUserEventRequests: participationRequestDtos={}", participationRequestDtos);
        return participationRequestDtos;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequests(@PathVariable int userId, @PathVariable int eventId,
                                                                  @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("updateUserEventRequests: {}", eventRequestStatusUpdateRequest);
        return eventService.updateEventRequestsPriv(userId, eventId, eventRequestStatusUpdateRequest);
    }

}
