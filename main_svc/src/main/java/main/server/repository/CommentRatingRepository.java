package main.server.repository;

import main.server.dao.CommentRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRatingRepository extends JpaRepository<CommentRating, Integer> {
}
