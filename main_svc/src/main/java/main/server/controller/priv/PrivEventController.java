package main.server.controller.priv;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.*;
import main.server.exception.ConflictException;
import main.server.service.CommentService;
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
    private final CommentService commentService;

    public PrivEventController(EventService eventService, CommentService commentService) {
        this.eventService = eventService;
        this.commentService = commentService;
    }

    @GetMapping("/{user-id}/events")
    public List<EventShortDto> getUserEvents(@PathVariable("user-id") int userId, @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("getUserEvents: userId={}, from={}, size={}", userId, from, size);
        List<EventShortDto> events = eventService.getEventsPriv(userId, from, size);
        log.info("getUserEvents: events={}", events);
        return events;
    }

    @PostMapping("{user-id}/events")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public EventFullDto addUserEvent(@PathVariable("user-id") int userId, @RequestBody NewEventDto newEventDto) {
        log.info("addUserEvent: userId={}, newEventDto={}", userId, newEventDto);
        newEventDto.validate();
        EventFullDto eventFullDto = eventService.createEventPriv(userId, newEventDto);
        log.info("addUserEvent: eventFullDto={}", eventFullDto);
        return eventFullDto;
    }


    @GetMapping("/{user-id}/events/{event-id}")
    public EventFullDto getUserEventById(@PathVariable("user-id") int userId, @PathVariable("event-id") int eventId) {
        log.info("getUserEventById: userId={}, eventId={}", userId, eventId);
        EventFullDto eventFullDto = eventService.getEventPriv(userId, eventId);
        log.info("getUserEventById: eventFullDto={}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/{user-id}/events/{event-id}")
    public EventFullDto updateUserEventById(@PathVariable("user-id") int userId, @PathVariable("event-id") int eventId,
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

    @GetMapping("/{user-id}/events/{event-id}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable("user-id") int userId, @PathVariable("event-id") int eventId) {
        log.info("getUserEventRequests: userId={}, eventId={}", userId, eventId);
        List<ParticipationRequestDto> participationRequestDtos = eventService.getEventRequestsPriv(userId, eventId);
        log.info("getUserEventRequests: participationRequestDtos={}", participationRequestDtos);
        return participationRequestDtos;
    }

    @PatchMapping("/{user-id}/events/{event-id}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequests(@PathVariable("user-id") int userId, @PathVariable("event-id") int eventId,
                                                                  @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("updateUserEventRequests: {}", eventRequestStatusUpdateRequest);
        return eventService.updateEventRequestsPriv(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @PostMapping("/{user-id}/events/{event-id}/comments")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public CommentDto addUserEventComment(@PathVariable("user-id") int userId, @PathVariable("event-id") int eventId,
                                          @RequestBody NewCommentDto newCommentDto) {
        log.info("addUserEventComment: userId={}, eventId={}, newCommentDto={}", userId, eventId, newCommentDto);
        newCommentDto.validate();
        CommentDto commentDto = commentService.addComment(newCommentDto,eventId, userId);
        log.info("addUserEventComment: commentDto={}", commentDto);
        return commentDto;
    }

    @PatchMapping("/{user-id}/events/{event-id}/comments/{comment-id}")
    public CommentDto updateUserEventComment(@PathVariable("user-id") int userId, @PathVariable("event-id") int eventId,
                                             @PathVariable("comment-id") int commentId, @RequestBody NewCommentDto newCommentDto) {
        log.info("updateUserEventComment: userId={}, eventId={}, commentId={}, newCommentDto={}", userId, eventId, commentId, newCommentDto);
        newCommentDto.validate();
        CommentDto commentDto = commentService.updateComment(newCommentDto, eventId, userId, commentId);
        log.info("updateUserEventComment: commentDto={}", commentDto);
        return commentDto;
    }
}
