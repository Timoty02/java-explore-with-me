package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Request;
import main.server.dto.ParticipationRequestDto;
import main.server.mapper.RequestMapper;
import main.server.repository.EventRepository;
import main.server.repository.RequestRepository;
import main.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RequestService(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }
    public List<ParticipationRequestDto> getRequests(int userId) {
        log.info("Getting requests for user: {}", userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Requests found: {}", requests);
        return requests.stream().map(RequestMapper::toDto).toList();
    }
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        log.info("Cancelling request: {} for user: {}", requestId, userId);
        Request request = requestRepository.findById(requestId).orElseThrow();
        request.setStatus("CANCELED");
        Request requestUp = requestRepository.save(request);
        log.info("Request cancelled: {}", requestUp);
        return RequestMapper.toDto(requestUp);
    }

    public ParticipationRequestDto addRequest(int userId, int eventId) {
        log.info("Adding request for event: {}, from user: {}", eventId, userId);
        Request request = new Request();
        request.setEvent(eventRepository.findById((eventId)).orElseThrow());
        if  (request.getEvent().isRequestModeration()) {
            request.setStatus("PENDING");
        } else {
            request.setStatus("CONFIRMED");
        }
        request.setRequester(userRepository.findById(userId).orElseThrow());
        Request requestUp = requestRepository.save(request);
        log.info("Request added:{}", requestUp);
        return RequestMapper.toDto(requestUp);
    }
}
