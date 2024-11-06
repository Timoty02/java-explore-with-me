package main.server.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.server.dto.CommentDto;
import main.server.dto.EventFullDto;
import main.server.dto.UpdateEventAdminRequest;
import main.server.service.CommentService;
import main.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;
    private final CommentService commentService;
    @Autowired
    public AdminEventController(EventService eventService, CommentService commentService) {
        this.eventService = eventService;
        this.commentService = commentService;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users, @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Integer> categories, @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd, @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("getEventsAdmin");
        List<EventFullDto> eventFullDtos = eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd,
                from, size);
        log.info("gotEvents: {}", eventFullDtos);
        return eventFullDtos;
    }

    @PatchMapping("/{event-id}")
    public EventFullDto updateEvent(@PathVariable("event-id") int eventId, @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("updateEvent");
        updateEventAdminRequest.validate();
        EventFullDto eventFullDto = eventService.updateEventAdmin(eventId, updateEventAdminRequest);
        log.info("updatedEvent: {}", eventFullDto);
        return eventFullDto;
    }
    @DeleteMapping("/{event-id}/comments/{comment-id}")
    @ResponseStatus(code = org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("event-id") int eventId, @PathVariable("comment-id") int commentId) {
        log.info("deleteComment with id:{}", commentId);
        commentService.deleteCommentAdmin(commentId);
        log.info("deletedComment");
    }
    @GetMapping("/{event-id}/comments/{comment-id}")
    public CommentDto getComment(@PathVariable("event-id") int eventId, @PathVariable("comment-id") int commentId) {
        log.info("getComment with id:{}", commentId);
        CommentDto commentDto = commentService.getCommentByIdAdmin(commentId);
        log.info("gotComment");
        return commentDto;
    }
}
