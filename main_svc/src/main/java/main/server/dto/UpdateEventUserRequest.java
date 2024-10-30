package main.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import main.server.dao.Event;
import main.server.dao.Location;

import java.time.LocalDateTime;

@Slf4j
@Data
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateEventUserRequest {
    String annotation;
    Integer category;
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    String title;

    public UpdateEventUserRequest(String annotation, Integer category, String description, String eventDate, Location location, Boolean paid, Integer participantLimit, Boolean requestModeration,
                                  String stateAction, String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.stateAction = stateAction;
        this.title = title;
    }

    public void validate() {
        validateParticipantLimit();
        validateAnnotation();
        validateDescription();
        validateEventDate();
        validateTitle();
    }

    private void validateParticipantLimit() {
        if (participantLimit != null && participantLimit < 0) {
            throw new IllegalArgumentException("Participant limit is not valid");
        }
    }

    private void validateAnnotation() {
        if (annotation != null && (annotation.isBlank() || annotation.length() < 20 || annotation.length() > 2000)) {
            throw new IllegalArgumentException("Annotation is not valid");
        }
    }

    private void validateDescription() {
        if (description != null && (description.isBlank() || description.length() < 20 || description.length() > 7000)) {
            throw new IllegalArgumentException("Description is not valid");
        }
    }

    private void validateEventDate() {
        if (eventDate != null && (eventDate.isBlank() || LocalDateTime.parse(eventDate, Event.DATE_TIME_FORMATTER).isBefore(LocalDateTime.now().plusHours(2)))) {
            throw new IllegalArgumentException("Event date is not valid");
        }
    }

    private void validateTitle() {
        if (title != null && (title.isBlank() || title.length() < 3 || title.length() > 120)) {
            throw new IllegalArgumentException("Title is not valid");
        }
    }
}
