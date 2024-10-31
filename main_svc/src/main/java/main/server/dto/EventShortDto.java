package main.server.dto;

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
public class EventShortDto {
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String eventDate;
    int id;
    UserShortDto initiator;
    boolean paid;
    String title;
    int views;
}
