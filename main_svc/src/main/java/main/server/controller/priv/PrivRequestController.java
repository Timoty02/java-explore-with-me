package main.server.controller.priv;

import lombok.extern.slf4j.Slf4j;
import main.server.dto.ParticipationRequestDto;
import main.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivRequestController {
    private final RequestService requestService;

    @Autowired
    public PrivRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/{user-id}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable("user-id") int userId) {
        log.info("Getting requests for user: {}", userId);
        List<ParticipationRequestDto> requests = requestService.getRequests(userId);
        log.info("Requests found: {}", requests.size());
        return requests;
    }

    @PostMapping("/{user-id}/requests")
    @ResponseStatus(value = org.springframework.http.HttpStatus.CREATED)
    public ParticipationRequestDto addUserRequest(@PathVariable("user-id") int userId, @RequestParam int eventId) {
        log.info("Adding request for event: {}, from user: {}", eventId, userId);
        ParticipationRequestDto request = requestService.addRequest(userId, eventId);
        log.info("Request added: {}", request);
        return request;
    }

    @PatchMapping("/{user-id}/requests/{request-id}/cancel")
    public ParticipationRequestDto cancelRequestById(@PathVariable("user-id") int userId, @PathVariable("request-id") int requestId) {
        log.info("Cancelling request: {} for user: {}", requestId, userId);
        ParticipationRequestDto request = requestService.cancelRequest(userId, requestId);
        log.info("Request cancelled: {}", request);
        return request;
    }
}
