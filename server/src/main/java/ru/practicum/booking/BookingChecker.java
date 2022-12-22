package ru.practicum.booking;

import ru.practicum.exceptions.BookerIsOwnerItemException;
import ru.practicum.exceptions.ItemIllegalArgumentException;
import ru.practicum.exceptions.UserNotOwnerItemException;

public interface BookingChecker<T extends Booking> {
    void check(T t) throws UserNotOwnerItemException, BookerIsOwnerItemException, ItemIllegalArgumentException;
}