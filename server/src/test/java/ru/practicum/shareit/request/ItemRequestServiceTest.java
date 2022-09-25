package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.ItemRequestServiceImpl;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(value = MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestServiceImpl;

    @Test
    public void shouldFailAddOnWrongUser() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestServiceImpl.add(1, ItemRequestCreateDto.builder()
                .id(1L).description("dd").build()));
    }

    @Test
    public void shouldAddRequest() throws UserNotFoundException {
        passUserValidation();

        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L).description("dd").build();
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestCreateDto, 1);

        Mockito.when(itemRequestRepository.save(itemRequest))
                .thenReturn(itemRequest);

        assertThat(itemRequestServiceImpl.add(1, itemRequestCreateDto), equalTo(itemRequest));
    }

    @Test
    public void shouldFailGetByIdOnWrongUser() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestServiceImpl.getById(1, anyInt())
        );
    }

    @Test
    public void shouldFailGetByIdOnWrongId() {
        passUserValidation();

        Assertions.assertThrows(ItemRequestNotFoundException.class,
                () -> itemRequestServiceImpl.getById(1, anyInt())
        );
    }

    @Test
    public void shouldGetById() throws UserNotFoundException, ItemRequestNotFoundException {
        passUserValidation();

        ItemRequest itemRequest = new ItemRequest();
        long id = 1L;
        itemRequest.setId(id);

        Mockito.when(itemRequestRepository.findById(id))
                .thenReturn(Optional.of(itemRequest));

        assertThat(itemRequestServiceImpl.getById(anyInt(), id), equalTo(itemRequest));
    }

    private void passUserValidation() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);
    }

    @Test
    public void shouldFailGetAllByRequesterId() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestServiceImpl.getAllByRequesterId(anyInt())
        );
    }

    @Test
    public void shouldGetAllByRequesterId() throws UserNotFoundException {
        passUserValidation();

        LocalDateTime moment = LocalDateTime.now().plusMinutes(1);
        Long requesterId = 4L;
        List<ItemRequest> itemRequestList = List.of(new ItemRequest(1L, "dd1", requesterId, moment),
                new ItemRequest(2L, "dd2", requesterId, moment.minusSeconds(10)),
                new ItemRequest(3L, "dd3", requesterId, moment.minusSeconds(20)));

        Mockito.when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(anyLong()))
                .thenReturn(itemRequestList);

        assertThat(itemRequestServiceImpl.getAllByRequesterId(requesterId), equalTo(itemRequestList));
    }

    @Test
    public void shouldGetAllOfOthersInPages() {
        LocalDateTime moment = LocalDateTime.now().plusMinutes(1);
        Long userId = 4L;
        List<ItemRequest> itemRequestList = List.of(new ItemRequest(1L, "dd1", userId, moment),
                new ItemRequest(2L, "dd2", userId, moment.minusSeconds(10)),
                new ItemRequest(3L, "dd3", userId, moment.minusSeconds(20)));
        Page<ItemRequest> itemRequestPage = new PageImpl<>(itemRequestList);

        Mockito.when(itemRequestRepository.findAllByRequesterIdNot(anyLong(), any()))
                .thenReturn(itemRequestPage);

        assertThat(itemRequestServiceImpl.getAllOfOthersInPages(userId, 0, 3),
                equalTo(itemRequestPage.getContent()));
    }
}