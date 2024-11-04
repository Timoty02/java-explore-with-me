package main.server.mapper;

import main.server.dao.Comment;
import main.server.dto.CommentDto;
import main.server.dto.NewCommentDto;

import java.time.format.DateTimeFormatter;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(UserMapper.toUserDto(comment.getAuthor()));
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        commentDto.setStatus(comment.getStatus());
        commentDto.setEventId(comment.getEventId());
        commentDto.setRating(comment.getRating());
        return commentDto;
    }
    public static Comment toComment(NewCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        return comment;
    }

}
