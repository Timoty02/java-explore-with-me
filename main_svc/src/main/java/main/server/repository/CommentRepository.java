package main.server.repository;

import main.server.dao.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    List<Comment> findAllByEventIdAndStatusIn(int eventId, List<String> published);
}
