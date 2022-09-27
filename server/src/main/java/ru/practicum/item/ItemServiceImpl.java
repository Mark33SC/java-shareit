package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.Status;
import ru.practicum.exceptions.CommentValidationException;
import ru.practicum.exceptions.ItemNotFoundException;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.exceptions.UserNotOwnerItemException;
import ru.practicum.item.comment.CommentRepository;
import ru.practicum.item.comment.Comment;
import ru.practicum.item.comment.dto.CommentCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.dto.ItemWithBookingDatesDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;
import ru.practicum.util.CustomPageable;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemWithBookingDatesDto> getAllItemsOfOwner(long ownerId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("id"));

        return itemRepository.findByOwnerId(ownerId, pageRequest).stream()
                .map(this::addToItemLastAndNextBooking)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ItemWithBookingDatesDto getItemById(long itemId, long userId) {

        Item item = getItemById(itemId);
        ItemWithBookingDatesDto itemDto = ItemMapper.toItemWithBookingDatesDto(item);

        if (isOwnerOfItem(userId, itemId)) {
            itemDto = addToItemLastAndNextBooking(item);
        }

        List<Comment> comments = getAllCommentsByItem(item);
        itemDto.setComments(
                comments.stream()
                        .map(c -> ItemWithBookingDatesDto.Comment.builder()
                                .id(c.getId())
                                .text(c.getText())
                                .authorName(c.getAuthor().getName())
                                .created(c.getCreated())
                                .build())
                        .collect(Collectors.toList())
        );

        return itemDto;
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        User owner = userService.getUserById(ownerId);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(long ownerId, ItemDto itemDto) {

        checkItemOwner(ownerId);

        if (!isOwnerOfItem(ownerId, itemDto.getId())) {
            throw new UserNotOwnerItemException();
        }

        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(ItemNotFoundException::new);

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        return item;
    }

    @Override
    public void deleteById(long ownerId, long id) {
        checkItemOwner(ownerId);
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return List.of();
        }

        Pageable page = CustomPageable.of(from, size, Sort.sort(Item.class).by(Item::getId).ascending());

        return itemRepository.searchItems(text, page).stream()
                .map(ItemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment addComment(CommentCreateDto commentCreateDto, long itemId, long userId) {

        User author = userService.getUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException(String.format("Item with id:%s not found.", itemId))
        );

        boolean authorIsBookedItem = bookingRepository.findAllBookingByItem(item).stream()
                .anyMatch(
                        b -> b.getBooker().getId().equals(userId)
                                && !b.getStatus().equals(Status.REJECTED)
                                && item.getAvailable()
                                && b.getEnd().isBefore(LocalDateTime.now())
                );

        if (!authorIsBookedItem) {
            throw new CommentValidationException(
                    String.format("User with id:%s did not book the item with id:%s", userId, itemId)
            );
        }

        Comment comment = Comment.builder()
                .text(commentCreateDto.getText())
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<Item> findAllByRequestId(long requestId) {
        return itemRepository.findAllByRequestId(requestId);
    }

    private List<Comment> getAllCommentsByItem(Item item) {
        return commentRepository.findAllCommentByItem(item);
    }

    private void checkItemOwner(long ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new UserNotFoundException();
        }
    }

    private boolean isOwnerOfItem(long userId, long itemId) {
        User owner = userService.getUserById(userId);
        Item item = getItemById(itemId);

        return item.getOwner().equals(owner);
    }

    private ItemWithBookingDatesDto addToItemLastAndNextBooking(Item item) {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findAllBookingByItemId(item.getId());

        Optional<Booking> lastBooking = bookings.stream()
                .filter(b -> b.getEnd().isBefore(now))
                .max(Comparator.comparing(Booking::getEnd));

        Optional<Booking> nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .min(Comparator.comparing(Booking::getStart));

        ItemWithBookingDatesDto itemWithBookingDatesDto = ItemMapper.toItemWithBookingDatesDto(item);

        lastBooking.ifPresent(itemWithBookingDatesDto::setLastBooking);
        nextBooking.ifPresent(itemWithBookingDatesDto::setNextBooking);

        return itemWithBookingDatesDto;
    }
}