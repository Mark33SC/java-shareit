package ru.practicum.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    List<Booking> findAllByBookerOrderByIdDesc(User booker, Pageable page);

    List<Booking> findAllBookingByItem(Item item);

    List<Booking> findAllBookingByItemId(long itemId);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN b.item Item " +
            "WHERE Item.owner=:owner " +
            "ORDER BY b.id DESC")
    List<Booking> findAllBookingsByItemOwner(@Param("owner") User owner, Pageable page);
}