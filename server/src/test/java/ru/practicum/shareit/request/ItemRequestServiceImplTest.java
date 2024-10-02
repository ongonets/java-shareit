package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
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
@Import({ItemRequestServiceImpl.class})
class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final ItemRequestService requestService;

    private ItemRequestDto sourceRequest = new ItemRequestDto(0, "description", LocalDateTime.now());

    private User getUser() {
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");
        User entity = UserMapper.mapToUser(sourceUser);
        em.persist(entity);
        em.flush();
        return entity;
    }

    private ItemRequest getRequest() {
        User user = getUser();
        ItemRequest entity = ItemRequestMapper.mapToItemRequest(sourceRequest, user);
        em.persist(entity);
        em.flush();
        return entity;
    }


    @Test
    void createItemRequest() {
        // given
        User user = getUser();
        long userId = user.getId();

        // when
        requestService.createItemRequest(userId, sourceRequest);

        // then
        TypedQuery<ItemRequest> query = em.createQuery(
                "Select r from ItemRequest r where r.description = :description",
                ItemRequest.class);
        ItemRequest targetRequest = query.setParameter("description", sourceRequest.getDescription())
                .getSingleResult();

        assertThat(targetRequest, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("requester", equalTo(user)),
                hasProperty("description", equalTo(sourceRequest.getDescription()))
        )));
    }

    @Test
    void findItemRequestById() {
        // given
        ItemRequest sourceRequest = getRequest();

        // when
        ItemRequestWithItem targetRequest = requestService.findItemRequestById(sourceRequest.getId());

        // then
        assertThat(targetRequest, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("description", equalTo(sourceRequest.getDescription()))
        )));
    }

    @Test
    void findRequesterItemRequests() {
        // given
        ItemRequest sourceRequest = getRequest();
        long userId = sourceRequest.getRequester().getId();

        // when
        Collection<ItemRequestWithItem> targetRequests = requestService.findRequesterItemRequests(userId);

        // then
        assertThat(targetRequests, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("description", equalTo(sourceRequest.getDescription()))
        )));
    }

    @Test
    void findAllItemRequests() {
        // given
        ItemRequest sourceRequest = getRequest();

        // when
        Collection<ItemRequestDto> targetRequests = requestService.findAllItemRequests();

        // then
        assertThat(targetRequests, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("description", equalTo(sourceRequest.getDescription()))
        )));
    }
}