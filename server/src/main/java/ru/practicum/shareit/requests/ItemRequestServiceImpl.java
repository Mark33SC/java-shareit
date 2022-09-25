package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.CustomPageable;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequest add(long requesterId, ItemRequestCreateDto itemRequestInputDto) throws UserNotFoundException {
        validateUserId(requesterId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestInputDto, requesterId);

        return itemRequestRepository.save(itemRequest);
    }

    private void validateUserId(long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public ItemRequest getById(long userId, long requestId) throws ItemRequestNotFoundException, UserNotFoundException {
        validateUserId(userId);
        return itemRequestRepository.findById(requestId).orElseThrow(ItemRequestNotFoundException::new);
    }

    @Override
    public List<ItemRequest> getAllByRequesterId(long requesterId) throws UserNotFoundException {
        validateUserId(requesterId);
        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(requesterId);
    }

    @Override
    public List<ItemRequest> getAllOfOthersInPages(long userId, Integer from, Integer size) {
        Pageable page = CustomPageable.of(from, size, Sort.sort(ItemRequest.class).by(ItemRequest::getCreated).descending());
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAllByRequesterIdNot(userId, page);

        return itemRequestPage.getContent();
    }
}
