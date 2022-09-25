package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream().map(UserDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        User user = userService.getUserById(id);

        return UserDtoMapper.mapToDto(user);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        User user = userService.addUser(
                UserDtoMapper.toUser(userCreateDto)
        );

        return UserDtoMapper.mapToDto(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateById(@PathVariable long id, @RequestBody UserDto userDto) {
        User user = userService.updateById(
                id,
                UserDtoMapper.toUser(userDto)
        );

        return UserDtoMapper.mapToDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);
    }
}