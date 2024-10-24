package main.server.controller.priv;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.EventRequestStatusUpdateRequest;
import main.server.dto.NewEventDto;
import main.server.dto.UpdateEventUserRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivEventController {
    @GetMapping("/{userId}/events")
    public String getUserEvents(@PathVariable int userId, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return "getUserEvents";
    }

    @PostMapping("{userId}/events")
    public String addUserEvent(@PathVariable int userId, @RequestBody NewEventDto newEventDto) {
        return "addUserEvent";
    }


    @GetMapping("/{userId}/events/{eventId}")
    public String getUserEventById(@PathVariable int userId, @PathVariable int eventId) {
        return "getUserEventById";
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public String updateUserEventById(@PathVariable int userId, @PathVariable int eventId, @RequestBody UpdateEventUserRequest newEventDto) {
        return "updateUserEventById";
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public String getUserEventRequests(@PathVariable int userId, @PathVariable int eventId) {
        return "getUserEventRequests";
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public String updateUserEventRequests(@PathVariable int userId, @PathVariable int eventId, @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return "updateUserEventRequests";
    }

}
