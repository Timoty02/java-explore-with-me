package main.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import main.server.dao.Location;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EventFullDto {
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    int id;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    int participantLimit;
    String publishedOn;
    boolean requestModeration;
    String state;
    String title;
    int views;
    List<CommentDto> comments = new ArrayList<>();

    public EventFullDto(String annotation, CategoryDto categoryDto, int confirmedRequests, String format, String description, String format1, int id, UserShortDto userShortDto, Location location, boolean paid, int participantLimit, String s, boolean requestModeration, String state, String title, int views) {
        this.annotation = annotation;
        this.category = categoryDto;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = format;
        this.description = description;
        this.eventDate = format1;
        this.id = id;
        this.initiator = userShortDto;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = s;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }
}
