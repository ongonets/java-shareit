package ru.practicum.shareit.request;

/**
 * TODO Sprint add-item-requests.
 */

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;

    String description;

    LocalDateTime created;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ItemRequest that = (ItemRequest) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
