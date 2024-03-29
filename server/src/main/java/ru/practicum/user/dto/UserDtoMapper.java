package ru.practicum.user.dto;

import ru.practicum.user.User;

public class UserDtoMapper {
    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static User toUser(UserCreateDto userCreateDto) {
        return new User(null, userCreateDto.getName(), userCreateDto.getEmail());
    }
}