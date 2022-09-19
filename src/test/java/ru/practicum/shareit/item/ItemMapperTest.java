package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemMapperTest {
    @Test
    public void checkToDtoCollection() {
        CommentCreateDto commentCreateDto = new CommentCreateDto("комментарий");

        Comment comment = CommentDtoMapper.toComment(commentCreateDto);

        assertThat(comment.getText(), equalTo(commentCreateDto.getText()));
    }
}
