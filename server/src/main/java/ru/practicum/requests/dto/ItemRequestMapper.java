package ru.practicum.requests.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.item.ItemService;
import ru.practicum.requests.ItemRequest;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final ItemService itemService;

    public ItemRequest toItemRequest(ItemRequestCreateDto itemRequestInputDto, long requesterId) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestInputDto.getDescription());
        itemRequest.setRequesterId(requesterId);
        return itemRequest;
    }

    public ItemRequestWithResponsesDto toDtoWithResponses(ItemRequest itemRequest) {
        return ItemRequestWithResponsesDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemService.findAllByRequestId(itemRequest.getId()))
                .build();
    }
}
