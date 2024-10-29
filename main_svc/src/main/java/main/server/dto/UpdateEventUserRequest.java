package main.server.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import main.server.dao.Location;

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
    private void validate() {
        if (participantLimit != null && participantLimit < 0) {
            throw new IllegalArgumentException("Participant limit must be positive");
        }
    }
}
