package ru.practicum.shareit.booking;

import ru.practicum.shareit.exceptions.BookerIsOwnerItemException;
import ru.practicum.shareit.exceptions.ItemIllegalArgumentException;
import ru.practicum.shareit.exceptions.UserNotOwnerItemException;

public interface BookingChecker<T extends Booking> {
    void check(T t) throws UserNotOwnerItemException, BookerIsOwnerItemException, ItemIllegalArgumentException;
}