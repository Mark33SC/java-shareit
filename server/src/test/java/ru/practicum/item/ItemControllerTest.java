package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Generators;
import ru.practicum.item.comment.Comment;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.dto.ItemWithBookingDatesDto;
import ru.practicum.user.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    Item item = new Item(1L, "name", "desc", true, new User(), null);
    ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("name")
            .description("desc")
            .build();
    ItemWithBookingDatesDto itemWithBookingDatesDto = ItemWithBookingDatesDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .build();

    @Test
    public void shouldAddItem() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));
    }

    @Test
    public void shouldGetAllItemsOfOwner() throws Exception {
        when(itemService.getAllItemsOfOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemWithBookingDatesDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemWithBookingDatesDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemWithBookingDatesDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemWithBookingDatesDto.getDescription()), String.class));
    }

    @Test
    public void shouldGetById() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDatesDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDatesDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDatesDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithBookingDatesDto.getDescription()), String.class));
    }

    @Test
    public void shouldUpdateById() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDatesDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDatesDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDatesDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithBookingDatesDto.getDescription()), String.class));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        mvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSearchItems() throws Exception {
        when(itemService.searchItems(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(ItemMapper.mapToDto(item)));

        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .param("from", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription()), String.class));
    }

    @Test
    public void shouldAddComment() throws Exception {
        Item item = Generators.ITEM_SUPPLIER.get();
        User user = Generators.USER_SUPPLIER.get();

        Comment comment = Comment.builder()
                .id(8L)
                .text("text")
                .item(item)
                .author(user)
                .build();

        when(itemService.addComment(Mockito.any(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(comment);

        mvc.perform(post("/items/6/comment")
                        .param("text", "sometext")
                        .param("from", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthor().getName()), String.class));
    }
}
