package main.server.service;

import lombok.extern.slf4j.Slf4j;
import main.server.dao.Comment;
import main.server.dao.User;
import main.server.dto.CommentDto;
import main.server.dto.NewCommentDto;
import main.server.exception.ConflictException;
import main.server.exception.NotFoundException;
import main.server.mapper.CommentMapper;
import main.server.repository.CommentRepository;
import main.server.repository.EventRepository;
import main.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
    public void deleteCommentAdmin(int commentId) {
        commentRepository.deleteById(commentId);
    }
    public void deleteCommentPrivate(int commentId, int userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("Comment not found"));
        if (comment.getAuthor().getId() != userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found")).getId()) {
            throw new ConflictException("You are not the author of this comment");
        }
        comment.setStatus("DELETED");
        commentRepository.save(comment);
    }
    public CommentDto  getCommentByIdAdmin(int commentId) {
        return (CommentMapper.toCommentDto(commentRepository.findById(commentId).orElseThrow()));
    }

    public CommentDto updateComment(NewCommentDto commentDto, int eventId, int userId, int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("Comment not found"));
        if (comment.getStatus().equals("DELETED")) {
            throw new ConflictException("Comment is deleted");
        }
        eventRepository.findById(eventId).orElseThrow(()->new NotFoundException("Event not found"));
        if (comment.getAuthor().getId() != userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found")).getId()) {
            throw new ConflictException("You are not the author of this comment");
        }
        comment.setText(commentDto.getText());
        comment.setStatus("UPDATED");
        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(updatedComment);
    }
    public CommentDto addComment(NewCommentDto commentDto,  int eventId, int userId) {
        log.info("Adding comment to event " + eventId + " by user " + userId);
        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
        eventRepository.findById(eventId).orElseThrow(()->new NotFoundException("Event not found"));
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setStatus("PUBLISHED");
        comment.setAuthor(user);
        comment.setEventId(eventId);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }
    public List<CommentDto> getCommentsByEventPub (int eventId) {
        List<Comment> comments = commentRepository.findAllByEventIdAndStatusIn(eventId,List.of( "PUBLISHED","UPDATED"));
        return comments.stream().map(CommentMapper::toCommentDto).toList();
    }
    public void rateComment(int commentId, int userId, String rating) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("Comment not found"));
        if (comment.getStatus().equals("DELETED")) {
            throw new ConflictException("Comment is deleted");
        }
        if (rating.equals("LIKE")) {
            comment.setRating(comment.getRating() + 1);
        } else if (rating.equals("DISLIKE")) {
            comment.setRating(comment.getRating() - 1);
        } else {
            throw new ConflictException("Rating must be PLUS or MINUS");
        }
        commentRepository.save(comment);
    }
}
