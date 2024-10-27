package main.server.dao;

import jakarta.persistence.*;
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
@Entity
@Table(name = "events")
public class Event {
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
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
    String publishedOn;
    boolean requestModeration;
    String state;
    String title;
    int views;

    public Event(String annotation, String description, String eventDate, float lat, float lng, boolean paid,
                 int participantLimit, boolean requestModeration, String title) {
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.locationLat = lat;
        this.locationLng = lng;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}
