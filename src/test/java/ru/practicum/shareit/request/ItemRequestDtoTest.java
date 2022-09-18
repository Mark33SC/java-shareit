package ru.practicum.shareit.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;
import ru.practicum.shareit.requests.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestCreateDto> itemRequestCreateDtoJacksonTester;
    @Autowired
    private JacksonTester<ItemRequestWithResponsesDto> itemRequestWithResponsesDtoJacksonTester;

    @Test
    public void testItemRequestInputDto() throws IOException {
        ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
                .id(1L)
                .description("dd")
                .build();

        JsonContent<ItemRequestCreateDto> jsonContent = itemRequestCreateDtoJacksonTester.write(itemRequestCreateDto);

        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("dd");
    }

    @Test
    public void testItemRequestWithResponsesDto() throws IOException {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Item> itemList = List.of(new Item(7L, "itemname", "itemdd", true, new User(), 9L));
        ItemRequestWithResponsesDto itemRequestWithResponsesDto = ItemRequestWithResponsesDto.builder()
                .id(1L)
                .description("dd")
                .created(now)
                .items(itemList)
                .build();

        JsonContent<ItemRequestWithResponsesDto> jsonContent = itemRequestWithResponsesDtoJacksonTester.write(itemRequestWithResponsesDto);

        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("dd");
        Assertions.assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo(now.toString());
        Assertions.assertThat(jsonContent).extractingJsonPathValue("$.items[0]")
                .hasFieldOrPropertyWithValue("id", 7);
    }
}
