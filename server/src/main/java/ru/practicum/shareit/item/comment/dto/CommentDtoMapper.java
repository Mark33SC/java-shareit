package ru.practicum.shareit.item.comment.dto;

import ru.practicum.shareit.item.comment.Comment;

public class CommentDtoMapper {
    public static CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentCreateDto commentCreateDto) {
        return Comment.builder().text(commentCreateDto.getText()).build();
    }
}