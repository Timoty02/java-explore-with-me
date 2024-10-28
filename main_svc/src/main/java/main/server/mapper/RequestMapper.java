package main.server.mapper;

import main.server.dao.Request;
import main.server.dto.ParticipationRequestDto;

public class RequestMapper {
    public static ParticipationRequestDto toDto(Request request) {
        return new ParticipationRequestDto(request.getCreated(), request.getEvent().getId(),
                request.getId(), request.getRequester().getId(), request.getStatus());
    }
}
