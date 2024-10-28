package main.server.mapper;

import main.server.dao.Event;
import main.server.dao.Location;
import main.server.dto.EventFullDto;
import main.server.dto.EventShortDto;
import main.server.dto.NewEventDto;

public class EventMapper {
    public static Event toEvent(NewEventDto eventDto) {
        return new Event(eventDto.getAnnotation(), eventDto.getDescription(), eventDto.getEventDate(),
                eventDto.getLocation().getLat(), eventDto.getLocation().getLng(), eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.isRequestModeration(), eventDto.getTitle());
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getCreatedOn(), event.getDescription(), event.getEventDate(),
                event.getId(), UserMapper.toUserShortDto(event.getInitiator()), new Location(event.getLocationLat(),
                event.getLocationLng()), event.isPaid(), event.getParticipantLimit(), event.getPublishedOn(),
                event.isRequestModeration(), event.getState(), event.getTitle(), event.getViews());
    }

    public static EventShortDto toEventShortDto (Event event){
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getEventDate(), event.getId(), UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(), event.getTitle(), event.getViews());
    }
}
