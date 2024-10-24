package main.server.controller.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivRequestController {

    @GetMapping("/{userId}/requests")
    public String getUserRequests(@PathVariable int userId) {
        return "getUserRequests";
    }

    @PostMapping("/{userId}/requests")
    public String addUserRequest(@PathVariable int userId, @RequestParam int eventId) {
        return "requestAdded";
    }

    @DeleteMapping("/{userId}/requests/{requestId}/cancel")
    public String getUserRequestById(@PathVariable int userId, @PathVariable int requestId) {
        return "cancelUserRequestById";
    }
}
