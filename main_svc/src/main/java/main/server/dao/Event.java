package main.server.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "events")
public class Event {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    int confirmedRequests;
    LocalDateTime createdOn = LocalDateTime.now();
    String description;
    LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @Column(name = "location_lat")
    float locationLat;
    @Column(name = "location_lng")
    float locationLng;
    boolean paid;
    int participantLimit;
    LocalDateTime publishedOn;
    boolean requestModeration;
    String state;
    String title;
    int views;

    public Event(String annotation, String description, String eventDate, float lat, float lng, boolean paid,
                 int participantLimit, boolean requestModeration, String title) {
        this.annotation = annotation;
        this.description = description;
        this.eventDate = LocalDateTime.parse(eventDate, DATE_TIME_FORMATTER);
        this.locationLat = lat;
        this.locationLng = lng;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }

    public void setState(String state) {
        this.state = state;
        if (state.equals("PUBLISHED")) {
            this.publishedOn = LocalDateTime.now();
        }
    }
}
