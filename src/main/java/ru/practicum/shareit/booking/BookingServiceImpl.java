package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.CustomPageable;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.BookingSpecs.*;
import static ru.practicum.shareit.booking.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;

    private final ItemService itemService;

    @Override
    public Booking addBooking(BookingCreateDto bookingCreateDto, long userId) {

        User booker = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingCreateDto.getItemId());

        Booking booking = BookingMapper.toBooking(bookingCreateDto);
        booking.setBooker(booker);
        booking.setItem(item);

        bookerIsNotOwnerItem.check(booking);
        bookingIsAvailable.check(booking);
        bookingDatesIsCorrect.check(booking);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings(long userId, String state, int from, int size) {
        User booker = userService.getUserById(userId);
        Status status = findByName(state);
        LocalDateTime now = LocalDateTime.now();
        Pageable pageRequest = CustomPageable.of(from, size, Sort.sort(Booking.class).by(Booking::getStart).descending());

        switch (status) {
            case CURRENT:
                return bookingRepository.findAll(
                                hasBooker(booker)
                                        .and(hasBookingStatus(APPROVED).or(hasBookingStatus(REJECTED)))
                                        .and(isBookingStartLessThan(now).and(isBookingEndBeforeThan(now))),
                                pageRequest)
                        .toList();

            case FUTURE:
                return bookingRepository.findAll(
                        hasBooker(booker)
                                .and(hasBookingStatus(APPROVED).or(hasBookingStatus(WAITING)))
                                .and(isBookingStartBeforeThan(now)),
                        pageRequest).toList();

            case WAITING:
                return bookingRepository.findAll(
                                hasBooker(booker)
                                        .and(hasBookingStatus(WAITING)),
                                pageRequest)
                        .toList();

            case REJECTED:
                return bookingRepository.findAll(
                                hasBooker(booker)
                                        .and(hasBookingStatus(REJECTED)),
                                pageRequest)
                        .toList();

            case PAST:
                return bookingRepository.findAll(
                                hasBooker(booker)
                                        .and(hasBookingStatus(APPROVED))
                                        .and(isBookingEndLessThan(now)),
                                pageRequest)
                        .toList();
        }

        return getAllByBooker(booker, pageRequest);
    }

    @Override
    public List<Booking> getAllBookingsForItemsOwner(long itemOwnerId, String state, int from, int size) {
        User itemOwner = userService.getUserById(itemOwnerId);
        Status status = findByName(state);
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(from, size, Sort.sort(Booking.class).by(Booking::getStart).descending());

        switch (status) {
            case CURRENT:
                return bookingRepository.findAll(
                                hasOwnerBookedItem(itemOwner)
                                        .and(hasBookingStatus(APPROVED)
                                                .or(hasBookingStatus(REJECTED)))
                                        .and(isBookingStartLessThan(now)
                                                .and(isBookingEndBeforeThan(now))),
                                pageRequest)
                        .toList();

            case FUTURE:
                return bookingRepository.findAll(
                                hasOwnerBookedItem(itemOwner)
                                        .and(hasBookingStatus(APPROVED)
                                                .or(hasBookingStatus(WAITING)))
                                        .and(isBookingStartBeforeThan(now)),
                                pageRequest)
                        .toList();

            case WAITING:
                return bookingRepository.findAll(
                                hasOwnerBookedItem(itemOwner)
                                        .and(hasBookingStatus(WAITING)),
                                pageRequest)
                        .toList();

            case REJECTED:
                return bookingRepository.findAll(
                                hasOwnerBookedItem(itemOwner)
                                        .and(hasBookingStatus(REJECTED)),
                                pageRequest)
                        .toList();

            case PAST:
                return bookingRepository.findAll(
                                hasOwnerBookedItem(itemOwner)
                                        .and(hasBookingStatus(APPROVED))
                                        .and(isBookingEndLessThan(now)),
                                pageRequest)
                        .toList();
        }
        return getAllByItemOwnerId(itemOwnerId, pageRequest);
    }

    @Override
    public Booking getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id:%s not found", bookingId)));

        User user = userService.getUserById(userId);

        accessIsAllowedToBooking.check(booking, user);

        return booking;
    }

    @Override
    public Booking approveBooking(long bookingId, long userId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id:%s not found", bookingId)));

        User user = userService.getUserById(userId);

        userIsOwnerBooking.check(booking, user);

        if (booking.getStatus().equals(APPROVED) && isApproved) {
            throw new BookingValidationException(String.format("Booking with id:%s already approved.", bookingId));
        }

        if (isApproved) {
            booking.setStatus(APPROVED);
        } else {
            booking.setStatus(REJECTED);
        }

        return booking;
    }

    @Override
    public List<Booking> getAllByItemOwnerId(long itemOwnerId, Pageable page) {
        User itemOwner = userService.getUserById(itemOwnerId);

        return bookingRepository.findAllBookingsByItemOwner(itemOwner, page);
    }

    @Override
    public List<Booking> getAllByBooker(User booker, Pageable page) {
        return bookingRepository.findAllByBookerOrderByIdDesc(booker, page);
    }

    private final BookingChecker<Booking> bookerIsNotOwnerItem = (booking) -> {
        boolean bookerIsOwnerItem = booking.getBooker().equals(booking.getItem().getOwner());
        if (bookerIsOwnerItem) {
            throw new BookerIsOwnerItemException(
                    String.format(
                            "Booker with id:%s is owner item with id:%s.",
                            booking.getBooker().getId(),
                            booking.getItem().getId()
                    )
            );
        }
    };

    private final BookingChecker<Booking> bookingIsAvailable = (booking) -> {
        if (!booking.getItem().getAvailable()) {
            throw new ItemIllegalArgumentException(
                    String.format("Item with id:%s is not available.", booking.getItem().getId())
            );
        }
    };

    private final BookingChecker<Booking> bookingDatesIsCorrect = (booking) -> {
        boolean isCorrectDates = booking.getStart().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getStart().isAfter(booking.getEnd());
        if (isCorrectDates) {
            throw new BookingIncorrectStartEndDatesException("Start or end of booking in the past");
        }
    };

    private final BookingAccessUserChecker<Booking, User> userIsOwnerBooking = (booking, user) -> {
        User itemOwner = booking.getItem().getOwner();

        if (!itemOwner.equals(user)) {
            throw new UserNotOwnerItemException(String.format("User with id: %s is not owner of item", user.getId()));
        }
    };

    private final BookingAccessUserChecker<Booking, User> accessIsAllowedToBooking = (booking, user) -> {
        if (!booking.getBooker().equals(user) && !booking.getItem().getOwner().equals(user)) {
            throw new UserNotOwnerItemException(String.format("Booking with id:%s access blocked.", booking.getId()));
        }
    };
}
