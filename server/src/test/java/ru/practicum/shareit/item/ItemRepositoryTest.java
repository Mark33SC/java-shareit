package ru.practicum.shareit.item;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.Generators;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.CustomPageable;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void verifySearchItems() {
        User user = Generators.USER_SUPPLIER.get();
        assertThat(user.getId(), nullValue());
        userRepository.save(user);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(1L));

        Item itemK = new Item(null, "пицца", "с сырным бортом", true, user, null);

        assertThat(itemK.getId(), nullValue());
        itemRepository.save(itemK);
        assertThat(itemK.getId(), notNullValue());
        assertThat(itemK.getId(), equalTo(1L));

        Item itemS = new Item(null, "ещё пицца", "с колбасным бортом", true, user, null);

        assertThat(itemS.getId(), nullValue());
        itemRepository.save(itemS);
        assertThat(itemS.getId(), notNullValue());
        assertThat(itemS.getId(), equalTo(2L));

        Pageable page = CustomPageable.of(0, 3, Sort.sort(Item.class).by(Item::getId).ascending());
        assertThat(itemRepository.searchItems("ss", page).getContent(), hasSize(0));

        List<Item> itemKResult = itemRepository.searchItems("сыр", page).getContent();
        assertThat(itemKResult, hasSize(1));
        assertThat(itemKResult, Matchers.hasItem(itemK));

        List<Item> itemSResult = itemRepository.searchItems("колб", page).getContent();
        assertThat(itemSResult, hasSize(1));
        assertThat(itemSResult, Matchers.hasItem(itemS));

        List<Item> result = itemRepository.searchItems("пиц", page).getContent();
        assertThat(result, hasSize(2));
        assertThat(result, Matchers.hasItem(itemS));
        assertThat(result, Matchers.hasItem(itemK));

        TypedQuery<Item> query = em.getEntityManager()
                .createQuery("SELECT it FROM Item it " +
                        "WHERE it.available = true " +
                        "AND (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%') ) " +
                        "OR LOWER(it.description) LIKE LOWER(concat('%', :text, '%') ))", Item.class);
        List<Item> resultFromQuery = query.setParameter("text", "пиц").getResultList();
        assertThat(result, equalTo(resultFromQuery));
    }
}