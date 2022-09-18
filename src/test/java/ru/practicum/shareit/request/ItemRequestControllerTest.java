package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Generators;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private ItemRequestMapper itemRequestMapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    ItemRequest itemRequest = new ItemRequest(1L, "dd", 10L, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
            .id(1L)
            .description("dd")
            .build();

    Item item = Generators.ITEM_SUPPLIER.get();

    ItemRequestWithResponsesDto itemRequestWithResponsesDto = ItemRequestWithResponsesDto.builder()
            .id(itemRequest.getId())
            .description(itemRequest.getDescription())
            .created(itemRequest.getCreated())
            .items(List.of(item))
            .build();

    @Test
    public void shouldAddItemRequest() throws Exception {
        when(itemRequestService.add(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription()), String.class))
                .andExpect(jsonPath("$.requesterId", is(itemRequest.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.created", is(itemRequest.getCreated().toString()), String.class));
    }

    @Test
    public void shouldGetById() throws Exception {
        when(itemRequestService.getById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(itemRequest);

        when(itemRequestMapper.toDtoWithResponses(ArgumentMatchers.any(ItemRequest.class)))
                .thenReturn(itemRequestWithResponsesDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestWithResponsesDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestWithResponsesDto.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(itemRequestWithResponsesDto.getCreated().toString()), String.class))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0]", is(item), Item.class));
    }

    @Test
    public void shouldGetAllByUserId() throws Exception {
        when(itemRequestMapper.toDtoWithResponses(ArgumentMatchers.any(ItemRequest.class)))
                .thenReturn(itemRequestWithResponsesDto);
        when(itemRequestService.getAllByRequesterId(anyLong()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemRequestWithResponsesDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestWithResponsesDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(itemRequestWithResponsesDto.getCreated().toString()), String.class))
                .andExpect(jsonPath("$.[0].items", hasSize(1)))
                .andExpect(jsonPath("$.[0].items[0]", is(item), Item.class));
    }

    @Test
    public void shouldGetAllOfOthersInPages() throws Exception {
        when(itemRequestMapper.toDtoWithResponses(ArgumentMatchers.any(ItemRequest.class)))
                .thenReturn(itemRequestWithResponsesDto);
        when(itemRequestService.getAllOfOthersInPages(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 10L)
                        .param("from", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemRequestWithResponsesDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestWithResponsesDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(itemRequestWithResponsesDto.getCreated().toString()), String.class))
                .andExpect(jsonPath("$.[0].items", hasSize(1)))
                .andExpect(jsonPath("$.[0].items[0]", is(item), Item.class));
    }
}
