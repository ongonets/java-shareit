package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserServiceImpl.class})
class UserServiceImplTest {


    private final EntityManager em;
    private final UserService userService;


    @Test
    void findAllUser() {
        // given
        List<NewUserRequest> sourceUsers = List.of(
                new NewUserRequest("Ivan", "ivan@mail.ru"),
                new NewUserRequest("Petr", "petr@mail.ru"),
                new NewUserRequest("Vasilii", "vasilii@mail.ru")
        );

        for (NewUserRequest user : sourceUsers) {
            User entity = UserMapper.mapToUser(user);
            em.persist(entity);
        }
        em.flush();

        // when
        Collection<UserDto> targetUsers = userService.findAllUser();

        // then
        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (NewUserRequest sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void findUserById() {
        // given
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");
        long id = userService.createUser(sourceUser).getId();


        // when
        UserDto targetUser = userService.findUserById(id);

        // then

        assertThat(targetUser, is(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceUser.getName())),
                hasProperty("email", equalTo(sourceUser.getEmail()))
        )));
    }


    @Test
    void createUser() {
        // given
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");

        // when
        userService.createUser(sourceUser);

        // then
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", sourceUser.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(sourceUser.getEmail()));
        assertThat(user.getName(), equalTo(sourceUser.getName()));
    }

    @Test
    void updateUser() {
        // given
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");
        long id = userService.createUser(sourceUser).getId();
        User updateUser = new User();
        updateUser.setName("Petr");

        // when
        userService.updateUser(id, updateUser);

        // then
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", sourceUser.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(sourceUser.getEmail()));
        assertThat(user.getName(), equalTo(updateUser.getName()));
    }

    @Test
    void deleteUser() {
        // given
        NewUserRequest sourceUser = new NewUserRequest("Ivan", "ivan@mail.ru");
        long id = userService.createUser(sourceUser).getId();

        // when
        userService.deleteUser(id);

        // then
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        Assertions.assertThrows(NoResultException.class,
                () -> query.setParameter("email", sourceUser.getEmail())
                .getSingleResult());

    }
}