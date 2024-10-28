package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.EventFullDto;
import main.server.dto.EventShortDto;
import main.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class PubEventController {
    private final EventService eventService;
    @Autowired
    public PubEventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getEvents");
        return eventService.getEventsPub(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable int id) {
        log.info("getEventById");
        return eventService.getEventByIdPub(id);
    }
}
