package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<User> getAll();

    User getById(long id) throws UserNotFoundException;

    User addUser(User user);

    User updateById(long userId, UserDto updatesOfUser) throws UserNotFoundException;

    void deleteById(long id);
}