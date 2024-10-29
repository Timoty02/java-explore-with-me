package main.server.mapper;

import main.server.dao.Event;
import main.server.dao.Location;
import main.server.dto.EventFullDto;
import main.server.dto.EventShortDto;
import main.server.dto.NewEventDto;

import java.util.Optional;

public class EventMapper {
    public static Event toEvent(NewEventDto eventDto) {
        return new Event(eventDto.getAnnotation(), eventDto.getDescription(), eventDto.getEventDate(),
                eventDto.getLocation().getLat(), eventDto.getLocation().getLon(), eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.isRequestModeration(), eventDto.getTitle());
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getCreatedOn().format(Event.DATE_TIME_FORMATTER), event.getDescription(), event.getEventDate().format(Event.DATE_TIME_FORMATTER),
                event.getId(), UserMapper.toUserShortDto(event.getInitiator()), new Location(event.getLocationLat(),
                event.getLocationLng()), event.isPaid(), event.getParticipantLimit(), Optional.ofNullable(event.getPublishedOn())
                .map(date -> date.format(Event.DATE_TIME_FORMATTER))
                .orElse(null),
                event.isRequestModeration(), event.getState(), event.getTitle(), event.getViews());
    }

    public static EventShortDto toEventShortDto (Event event){
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getEventDate().format(Event.DATE_TIME_FORMATTER), event.getId(), UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(), event.getTitle(), event.getViews());
    }
}
