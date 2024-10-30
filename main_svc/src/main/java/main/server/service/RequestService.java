package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Request;
import main.server.dto.ParticipationRequestDto;
import main.server.exception.ConflictException;
import main.server.exception.NotFoundException;
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
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Requests found: {}", requests);
        return requests.stream().map(RequestMapper::toDto).toList();
    }
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        log.info("Cancelling request: {} for user: {}", requestId, userId);
        Request request = requestRepository.findById(requestId).orElseThrow( () ->
                new NotFoundException("Request with id=" + requestId + " was not found"));
        request.setStatus("CANCELED");
        Request requestUp = requestRepository.save(request);
        log.info("Request cancelled: {}", requestUp);
        return RequestMapper.toDto(requestUp);
    }

    public ParticipationRequestDto addRequest(int userId, int eventId) {
        log.info("Adding request for event: {}, from user: {}", eventId, userId);
        Request request = new Request();
        request.setEvent(eventRepository.findById((eventId)).orElseThrow( () ->
                new NotFoundException("Event with id=" + eventId + " was not found")));
        if  (!request.getEvent().getState().equals("PUBLISHED")) {
            throw new ConflictException("Event is not PUBLISHED");
        }
        if  (request.getEvent().getParticipantLimit() != 0 && request.getEvent().getParticipantLimit() <= request.getEvent().getConfirmedRequests()) {
            throw new ConflictException("Participant limit has been reached");
        }
        if  (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Request already exists");
        }
        if  (request.getEvent().getInitiator().getId() == userId) {
            throw new ConflictException("Initiator cannot add request to own event");
        }
        if  (request.getEvent().isRequestModeration() && request.getEvent().getParticipantLimit() != 0) {
            request.setStatus("PENDING");
        } else {
            request.setStatus("CONFIRMED");
        }
        try {
            request.setRequester(userRepository.findById(userId).orElseThrow());
            Request requestUp = requestRepository.save(request);
            log.info("Request added:{}", requestUp);
            return RequestMapper.toDto(requestUp);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }
}
