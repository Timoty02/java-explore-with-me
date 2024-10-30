package main.server.controller.pub;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import main.server.dao.Event;
import main.server.dto.EventFullDto;
import main.server.dto.EventShortDto;
import main.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        log.info("getEvents");
        if (rangeStart != null && rangeEnd != null && LocalDateTime.parse(rangeStart, Event.DATE_TIME_FORMATTER).isAfter(LocalDateTime.parse(rangeEnd, Event.DATE_TIME_FORMATTER))) {
            log.info("getEvents: rangeStart is after rangeEnd");
            throw new IllegalArgumentException("rangeStart is after rangeEnd");
        }
        List<EventShortDto> eventShortDtoList = eventService.getEventsPub(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        log.info("getEvents: eventShortDtoList={}", eventShortDtoList);
        return eventShortDtoList;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable int id, HttpServletRequest request) {
        log.info("getEventById");
        return eventService.getEventByIdPub(id, request);
    }
}
