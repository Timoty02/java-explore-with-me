package main.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class NewEventDto {

    String annotation;
    int category;

    String description;
    String eventDate;
    Location location;
    boolean paid;

    int participantLimit;
    boolean requestModeration;
    String title;

    public NewEventDto(String annotation, int category, String description, String eventDate, Location location, boolean paid, int participantLimit, boolean requestModeration, String title){
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }

    public void validate() throws IllegalArgumentException {
        validateAnnotation();
        validateDescription();
        validateParticipantLimit();
    }
    private void validateAnnotation() throws IllegalArgumentException {
        if (annotation == null || annotation.isBlank()) {
            throw new IllegalArgumentException("Annotation is required");
        }
    }
    private void validateDescription() throws IllegalArgumentException {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
    }
    private void validateParticipantLimit() throws IllegalArgumentException {
        if (participantLimit < 0) {
            throw new IllegalArgumentException("Participant limit must be non-negative");
        }
    }
}
