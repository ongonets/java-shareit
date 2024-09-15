package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i = ?1 and b.end < ?2 " +
            "order by b.end desc " +
            "limit 1")
    Optional<Booking> findLastBookings (Item item, LocalDateTime now);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i = ?1 and b.end > ?2 " +
            "order by b.end asc " +
            "limit 1")
    Optional<Booking> findNextBookings(Item item, LocalDateTime now);

    List<Booking> findByItemAndBookerAndStatusAndEndBefore(Item item,
                                                           User user,
                                                           BookingStatus status,
                                                           LocalDateTime localDateTime);
}
