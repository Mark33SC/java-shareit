package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface UserRepository {

    Collection<User> getAll();

    User getById(long id) throws UserNotFoundException;

    User addUser(User user);

    User updateById(User user);

    void deleteById(long id);
}