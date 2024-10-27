package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.UpdateEventAdminRequest;
import main.server.dto.EventFullDto;
import main.server.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Integer> users, @RequestParam List<String> states,
                                        @RequestParam List<Integer> categories, @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd, @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("getEventsAdmin");
        List<EventFullDto> eventFullDtos = eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd,
                from, size);
        log.info("gotEvents: {}", eventFullDtos);
        return eventFullDtos;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable int eventId,@RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("updateEvent");
        EventFullDto eventFullDto = eventService.updateEventAdmin(eventId, updateEventAdminRequest);
        log.info("updatedEvent: {}", eventFullDto);
        return eventFullDto;
    }
}
