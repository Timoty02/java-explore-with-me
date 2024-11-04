package main.server.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "comment_rating")
@IdClass(CommentRatingId.class)
public class CommentRating {
    @Id
    int commentId;
    @Id
    int userId;
    String state;
}
class CommentRatingId implements Serializable {
    private long commentId;
    private long userId;
}
