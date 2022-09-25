package ru.practicum.shareit.booking;

import ru.practicum.shareit.exceptions.UserNotOwnerItemException;
import ru.practicum.shareit.user.User;

public interface BookingAccessUserChecker<T extends Booking, U extends User> {
    void check(T t, U u) throws UserNotOwnerItemException;
}