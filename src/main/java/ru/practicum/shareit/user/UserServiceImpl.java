package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(long id) throws UserNotFoundException {
        return userRepository.getById(id);
    }


    @Override
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public User updateById(long userId, UserDto userDto) throws UserNotFoundException {
        User user = userRepository.getById(userId);

        User userCopy = new User(user.getId(), user.getName(),
                user.getEmail(), user.getItemSharingId());
        if (userDto.getName() != null) {
            userCopy.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userCopy.setEmail(userDto.getEmail());
        }
        return userRepository.updateById(userCopy);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}