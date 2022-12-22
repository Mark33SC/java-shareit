package ru.practicum.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT item FROM Item item " +
            "WHERE item.available=true " +
            "AND (LOWER(item.name) LIKE LOWER(CONCAT('%', :text, '%') ) " +
            "OR LOWER(item.description) LIKE LOWER(CONCAT('%', :text, '%') ))")
    Page<Item> searchItems(String text, Pageable page);

    List<Item> findAllByRequestId(long requestId);
}