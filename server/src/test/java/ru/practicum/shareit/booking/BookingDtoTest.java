package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingBookerIdDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingCreateDto> inputDtoJacksonTester;
    @Autowired
    private JacksonTester<BookingDto> outputDtoJacksonTester;
    @Autowired
    private JacksonTester<BookingBookerIdDto> bookerIdDtoJacksonTester;


    LocalDateTime nowPlus10s = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(10);
    LocalDateTime nowPlusMin = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.SECONDS);

//    @Test
//    public void checkCreateDto() throws Exception {
//        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
//                .id(1L)
//                .start(nowPlus10s)
//                .end(nowPlusMin)
//                .itemId(10L)
//                .build();
//
//        JsonContent<BookingCreateDto> result = inputDtoJacksonTester.write(bookingCreateDto);
//
//        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(nowPlus10s.toString());
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(nowPlusMin.toString());
//        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(10);
//    }

//    @Test
//    public void checkDto() throws Exception {
//        BookingDto bookingDto = BookingDto.builder()
//                .id(1L)
//                .start(nowPlus10s)
//                .end(nowPlusMin)
//                .build();
//
//        JsonContent<BookingDto> result = outputDtoJacksonTester.write(bookingDto);
//
//        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(nowPlus10s.toString());
//        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(nowPlusMin.toString());
//    }

    @Test
    public void checkBookerIdDto() throws Exception {
        BookingBookerIdDto bookingBookerIdDto = new BookingBookerIdDto(1L, 14L);

        JsonContent<BookingBookerIdDto> result = bookerIdDtoJacksonTester.write(bookingBookerIdDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(14);
    }
}
