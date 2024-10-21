package main.server.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateEventUserRequest {
    String annotation;
    int category;
    String description;
    String eventDate;
    Location location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    String stateAction;
    String title;
}
