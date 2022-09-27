package ru.practicum.booking;

import ru.practicum.exceptions.UserNotOwnerItemException;
import ru.practicum.user.User;

public interface BookingAccessUserChecker<T extends Booking, U extends User> {
    void check(T t, U u) throws UserNotOwnerItemException;
}