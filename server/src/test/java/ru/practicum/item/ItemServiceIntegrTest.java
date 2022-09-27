package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.dto.BookingCreateDto;
import ru.practicum.exceptions.BookingNotFoundException;
import ru.practicum.exceptions.BookingValidationException;
import ru.practicum.exceptions.ItemNotFoundException;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.item.dto.ItemWithBookingDatesDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrTest {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    public void shouldGetAllItemsOfOwner() throws UserNotFoundException, BookingNotFoundException, BookingValidationException, ItemNotFoundException {
        User user = new User(null, "user_name", "useremail@ya.ru");
        user = userService.addUser(user);

        assertThat(user, hasProperty("id", equalTo(1L)));

        Item item1 = new Item();
        item1.setName("item1_name");
        item1.setDescription("dd1");
        item1.setOwner(user);
        item1.setAvailable(true);

        item1 = itemService.addItem(1L, item1);
        assertThat(item1, hasProperty("id", equalTo(1L)));

        Item item2 = new Item();
        item2.setName("item2_name");
        item2.setDescription("dd2");
        item2.setOwner(user);
        item2.setAvailable(true);

        item2 = itemService.addItem(1L, item2);
        assertThat(item2, hasProperty("id", equalTo(2L)));

        MatcherAssert.assertThat(itemService.getAllItemsOfOwner(1L, 0, 3), hasSize(2));

        User booker = new User(null, "booker_name", "bookeremail@ya.ru");
        User bookerSaved = userService.addUser(booker);

        BookingCreateDto bookingDto1OfItem1 = BookingCreateDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        bookingService.addBooking(bookingDto1OfItem1, bookerSaved.getId());

        BookingCreateDto bookingDto2OfItem1 = BookingCreateDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(4))
                .build();
        bookingService.addBooking(bookingDto2OfItem1, bookerSaved.getId());

        BookingCreateDto bookingDto1OfItem2 = BookingCreateDto.builder()
                .itemId(item2.getId())
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(6))
                .build();
        bookingService.addBooking(bookingDto1OfItem2, bookerSaved.getId());

        BookingCreateDto bookingDto2OfItem2 = BookingCreateDto.builder()
                .itemId(item2.getId())
                .start(LocalDateTime.now().plusMinutes(25))
                .end(LocalDateTime.now().plusMinutes(30))
                .build();
        bookingService.addBooking(bookingDto2OfItem2, bookerSaved.getId());

        Collection<ItemWithBookingDatesDto> itemsWithBookingsCollection = itemService.getAllItemsOfOwner(1, 0, 3);
        assertThat(itemsWithBookingsCollection, hasSize(2));
        assertThat(itemsWithBookingsCollection.toArray()[0],
                hasProperty("lastBooking"));
        assertThat(itemsWithBookingsCollection.toArray()[0],
                hasProperty("nextBooking"));
        assertThat(itemsWithBookingsCollection.toArray()[1],
                hasProperty("lastBooking"));
        assertThat(itemsWithBookingsCollection.toArray()[1],
                hasProperty("nextBooking"));
    }
}
