package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @NotBlank @Valid UserCreateDto userCreateDto) {
        return userClient.create(userCreateDto);
    }


    @GetMapping("{id}")
    public ResponseEntity<Object> get(@PathVariable long id) {
        return userClient.getById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable long id) {
        return userClient.update(userDto, id);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        return userClient.deleteById(userId);
    }

}


