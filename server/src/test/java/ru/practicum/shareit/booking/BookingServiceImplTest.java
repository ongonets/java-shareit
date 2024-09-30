package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({BookingServiceImpl.class})
class BookingServiceImplTest {

    private final EntityManager em;
    private final BookingService bookingService;

    NewBookingRequest sourceBooking = new NewBookingRequest(
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            1);

    private User getUser() {
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");
        User entity = UserMapper.mapToUser(sourceUser);
        em.persist(entity);
        em.flush();
        return entity;
    }

    private Item getItem(User user) {
        NewItemRequest sourceItem = new NewItemRequest("name",
                "description",
                true,
                null);
        Item entity = ItemMapper.mapToItem(sourceItem, user, null);
        em.persist(entity);
        em.flush();
        return entity;
    }

    private Booking getBooking() {
        User user = getUser();
        Item item = getItem(user);

        User booker = getUser();
        Booking entity = BookingMapper.mapToBooking(sourceBooking, item, booker);
        entity.setStatus(BookingStatus.WAITING);
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Test
    void createBooking() {
        // given
        User user = getUser();
        Item item = getItem(user);

        User booker = getUser();
        sourceBooking.setItemId(item.getId());


        // when
        bookingService.createBooking(booker.getId(), sourceBooking);

        // then
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.item = :item", Booking.class);
        Booking targetBooking = query.setParameter("item", item).getSingleResult();

        assertThat(targetBooking, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("item", equalTo(item)),
                hasProperty("booker", equalTo(booker)),
                hasProperty("start", equalTo(sourceBooking.getStart())),
                hasProperty("end", equalTo(sourceBooking.getEnd())),
                hasProperty("status", equalTo(BookingStatus.WAITING))
        )));
    }

    @Test
    void updateBooking() {
        // given
        Booking booking = getBooking();
        long ownerId = booking.getItem().getUser().getId();
        long bookingId = booking.getId();


        // when
        bookingService.updateBooking(ownerId, bookingId, true);

        // then
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :bookingId", Booking.class);
        Booking targetBooking = query.setParameter("bookingId", bookingId).getSingleResult();

        assertThat(targetBooking, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("item", equalTo(booking.getItem())),
                hasProperty("booker", equalTo(booking.getBooker())),
                hasProperty("start", equalTo(booking.getStart())),
                hasProperty("end", equalTo(booking.getEnd())),
                hasProperty("status", equalTo(BookingStatus.APPROVED))
        )));
    }

    @Test
    void findUserBooking() {
        // given
        Booking booking = getBooking();
        long userId = booking.getBooker().getId();
        long bookingId = booking.getId();


        // when
        BookingDto targetBooking = bookingService.findUserBooking(userId, bookingId);

        // then
        assertThat(targetBooking, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("start", equalTo(booking.getStart())),
                hasProperty("end", equalTo(booking.getEnd())),
                hasProperty("status", equalTo(BookingStatus.WAITING))
        )));
    }

    @Test
    void findBookerBookings() {
        // given
        Booking booking = getBooking();
        long userId = booking.getBooker().getId();


        // when
        Collection<BookingDto> targetBookings = bookingService.findBookerBookings(userId, BookingState.ALL);

        // then
        assertThat(targetBookings, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("start", equalTo(booking.getStart())),
                hasProperty("end", equalTo(booking.getEnd())),
                hasProperty("status", equalTo(BookingStatus.WAITING))
        )));
    }

    @Test
    void findOwnerBookings() {
        // given
        Booking booking = getBooking();
        long userId = booking.getItem().getUser().getId();


        // when
        Collection<BookingDto> targetBookings = bookingService.findOwnerBookings(userId, BookingState.ALL);

        // then
        assertThat(targetBookings, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("start", equalTo(booking.getStart())),
                hasProperty("end", equalTo(booking.getEnd())),
                hasProperty("status", equalTo(BookingStatus.WAITING))
        )));
    }
}