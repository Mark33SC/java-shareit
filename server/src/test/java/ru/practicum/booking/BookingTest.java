package ru.practicum.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class BookingTest {

    private Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), null);
    private Booking equalBooking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), null);
    private Booking otherBooking = new Booking(7L, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), null);


    @Test
    public void checkEquals() {
        assertThat(booking, equalTo(equalBooking));
        assertThat(booking, not(otherBooking));
    }

    @Test
    public void checkHashCode() {
        assertThat(booking.hashCode(), equalTo(equalBooking.hashCode()));
        assertThat(booking.hashCode(), not(otherBooking.hashCode()));
    }
}
