package main.server.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/events")
public class PubEventController {
    @GetMapping
    public String getEvents() {
        return "getEvents";
    }

    @GetMapping("/{id}")
    public String getEventById(@PathVariable int id) {
        return "getEventById";
    }
}
