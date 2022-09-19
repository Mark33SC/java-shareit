package ru.practicum.shareit.item;

import lombok.Generated;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Generators;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemMapperTest {
    private CommentCreateDto commentCreateDto = new CommentCreateDto("комментарий");


    @Test
    public void checkToDtoCollection() {
        Comment comment = CommentDtoMapper.toComment(commentCreateDto);

        assertThat(comment.getText(), equalTo(commentCreateDto.getText()));
    }
}
