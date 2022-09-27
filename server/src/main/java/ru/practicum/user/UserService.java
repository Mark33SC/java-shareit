package ru.practicum.user;

import java.util.Collection;

public interface UserService {

    Collection<User> getAll();

    User getUserById(long id);

    User addUser(User user);

    User updateById(long id, User userDto);

    void deleteById(long id);
}