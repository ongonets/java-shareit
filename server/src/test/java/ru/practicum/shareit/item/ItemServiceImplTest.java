package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
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
@Import({ItemServiceImpl.class})
class ItemServiceImplTest {


    private final EntityManager em;
    private final ItemService itemService;

    private final NewItemRequest sourceItem = new NewItemRequest("name",
            "description",
            true,
            null);

    private User getUser() {
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");
        User entity = UserMapper.mapToUser(sourceUser);
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Test
    void findItemById() {
        // given
        Item entity = ItemMapper.mapToItem(sourceItem, getUser(), null);
        em.persist(entity);
        em.flush();
        long id = entity.getId();

        // when
        ItemWithBookingDto targetItem = itemService.findItemById(id);

        // then
        assertThat(targetItem, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("available", equalTo(sourceItem.getAvailable())),
                hasProperty("description", equalTo(sourceItem.getDescription())))));
    }

    @Test
    void findByText() {
        // given
        User user = getUser();
        Item entity = ItemMapper.mapToItem(sourceItem, user, null);
        em.persist(entity);
        em.flush();

        // when
        Collection<ItemDto> targetItems = itemService.findByText(sourceItem.getName());

        // then
        assertThat(targetItems, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("available", equalTo(sourceItem.getAvailable())),
                hasProperty("description", equalTo(sourceItem.getDescription())))));
    }

    @Test
    void findOwnerItems() {
        // given
        User user = getUser();
        Item entity = ItemMapper.mapToItem(sourceItem, user, null);
        em.persist(entity);
        em.flush();

        // when
        Collection<ItemWithBookingDto> targetItems = itemService.findOwnerItems(user.getId());

        // then
        assertThat(targetItems, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("available", equalTo(sourceItem.getAvailable())),
                hasProperty("description", equalTo(sourceItem.getDescription())))));
    }

    @Test
    void createItem() {
        // given

        // when
        itemService.createItem(getUser().getId(), sourceItem);

        // then
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        Item targetItem = query.setParameter("name", sourceItem.getName()).getSingleResult();

        assertThat(targetItem, is(allOf(hasProperty("id",
                        notNullValue()), hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("available", equalTo(sourceItem.getAvailable())),
                hasProperty("description", equalTo(sourceItem.getDescription())))));
    }

    @Test
    void updateItem() {
        // given
        User user = getUser();
        Item entity = ItemMapper.mapToItem(sourceItem, user, null);
        em.persist(entity);
        em.flush();
        long id = entity.getId();
        NewItemRequest updateItem = new NewItemRequest("name", "newDescription", true, null);


        // when
        itemService.updateItem(id, user.getId(), updateItem);

        // then
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        Item targetItem = query.setParameter("name", sourceItem.getName()).getSingleResult();

        assertThat(targetItem, is(allOf(hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("available", equalTo(sourceItem.getAvailable())),
                hasProperty("description", equalTo(updateItem.getDescription())))));
    }

    @Test
    void deleteItem() {
        // given
        Item entity = ItemMapper.mapToItem(sourceItem, getUser(), null);
        em.persist(entity);
        em.flush();
        long id = entity.getId();

        // when
        itemService.deleteItem(id);

        // then
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);

        Assertions.assertThrows(NoResultException.class,
                () -> query.setParameter("name", sourceItem.getName()).getSingleResult());
    }

    @Test
    void createComment() {
        // given
        User user = getUser();

        Item entity = ItemMapper.mapToItem(sourceItem, user, null);
        em.persist(entity);
        em.flush();
        long id = entity.getId();

        NewBookingRequest bookingRequest = new NewBookingRequest(0,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                0,
                BookingStatus.APPROVED);
        Booking booking = BookingMapper.mapToBooking(bookingRequest, entity, user);
        em.persist(booking);
        em.flush();

        NewCommentRequest sourceComment = new NewCommentRequest("text");


        // when
        itemService.createComment(user.getId(), id, sourceComment);

        // then
        TypedQuery<Comment> query = em.createQuery("Select c from Comment c where c.text = :text", Comment.class);
        Comment targetComment = query.setParameter("text", sourceComment.getText()).getSingleResult();

        assertThat(targetComment, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("text", equalTo(sourceComment.getText())))));
    }
}