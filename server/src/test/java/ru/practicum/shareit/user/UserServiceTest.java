package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.Generators;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    @Autowired
    private UserService userService;

//    @Test
//    public void createUserTest() {
//        User user = Generators.USER_SUPPLIER.get();
//
//        Assertions.assertAll(
//                () -> Assertions.assertDoesNotThrow(() -> userService.addUser(user), "User create"),
//                () -> Assertions.assertNotNull(user.getId(), "User id is not null"),
//                () -> Assertions.assertThrows(
//                        DataIntegrityViolationException.class,
//                        () -> {
//                            user.setId(null);
//                            userService.addUser(user);
//                        },
//                        "User create duplicate email"
//                ),
//                () -> Assertions.assertThrows(
//                        DataIntegrityViolationException.class,
//                        () -> {
//                            user.setEmail(null);
//                            userService.addUser(user);
//                        },
//                        "User create fail no email"
//                ),
//                () -> Assertions.assertThrows(
//                        ConstraintViolationException.class,
//                        () -> {
//                            user.setEmail("user.com");
//                            userService.addUser(user);
//                        },
//                        "User create fail invalid email"
//                )
//        );
//    }

    @Test
    public void userUpdateTest() {
        User user1 = Generators.USER_SUPPLIER.get();
        User user2 = Generators.USER_SUPPLIER.get();
        userService.addUser(user1);

        User updatedUser = new User(null, "update", "update@user.com");
        User updateUserName = new User(null, "updateName", null);
        User updateUserEmail = new User(null, null, "updateName@user.com");
        User updateUserEmailExists = new User(null, null, user2.getEmail());

        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.updateById(user1.getId(), updatedUser), "User update"),
                () -> userService.addUser(user2),
                () -> assertEquals(
                        userService.updateById(user1.getId(), updateUserName).getName(),
                        "updateName",
                        "User name update"
                ),
                () -> assertEquals(
                        userService.updateById(user1.getId(), updateUserEmail).getEmail(),
                        "updateName@user.com",
                        "User name update email"
                ),
                () -> Assertions.assertThrows(DataIntegrityViolationException.class,
                        () -> userService.updateById(user1.getId(), updateUserEmailExists),
                        "User name update email exists"
                )
        );

    }

    @Test
    public void userGetTest() {
        User user = Generators.USER_SUPPLIER.get();
        userService.addUser(user);

        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.getUserById(user.getId()), "Get user"),
                () -> assertThrows(
                        UserNotFoundException.class,
                        () -> userService.getUserById(-1),
                        "User get unknown"
                )
        );
    }

    @Test
    public void userDeleteTest() {
        User user = Generators.USER_SUPPLIER.get();
        userService.addUser(user);

        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> userService.deleteById(user.getId()), "Delete user"),
                () -> Assertions.assertDoesNotThrow(() -> {
                    user.setId(null);
                    userService.addUser(user);
                }, "User create after delete")
        );
    }

    @Test
    public void userGetAllTest() {

        Assertions.assertAll(
                () -> Assertions.assertTrue(userService.getAll().size() > 0)
        );
    }
}
