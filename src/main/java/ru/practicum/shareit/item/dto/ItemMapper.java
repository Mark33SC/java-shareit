package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;

public class ItemMapper {
    public static ItemDto mapToDto(Item item) {
        ItemDto.ItemDtoBuilder result = ItemDto.builder();
        result.id(item.getId());
        result.name(item.getName());
        result.description(item.getDescription());
        result.available(item.getAvailable());
        result.requestId(item.getRequestId());

        return result.build();
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }

    public static ItemWithBookingDatesDto toItemWithBookingDatesDto(Item item) {

        return ItemWithBookingDatesDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}