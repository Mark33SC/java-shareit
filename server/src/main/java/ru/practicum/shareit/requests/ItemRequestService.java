package ru.practicum.shareit.requests;

import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequest add(long requesterId, ItemRequestCreateDto itemRequestInputDto) throws UserNotFoundException;

    ItemRequest getById(long userId, long requestId) throws ItemRequestNotFoundException, UserNotFoundException;

    List<ItemRequest> getAllByRequesterId(long requesterId) throws UserNotFoundException;

    List<ItemRequest> getAllOfOthersInPages(long userId, Integer from, Integer size);
}
