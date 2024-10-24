package main.server.controller.admin;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.UpdateEventAdminRequest;
import okhttp3.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventController {

    @GetMapping
    public String getEvents(@RequestParam List<Integer> users, @RequestParam List<String> states,
                            @RequestParam List<Integer> categories, @RequestParam String rangeStart,
                            @RequestParam String rangeEnd, @RequestParam(defaultValue = "0") int from,
                            @RequestParam(defaultValue = "10") int size) {
        log.info("getEvents");
        return "getEvents";
    }

    @PatchMapping("/{eventId}")
    public String updateEvent(@PathVariable int eventId,@RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("updateEvent");
        return "updateEvent";
    }
}
