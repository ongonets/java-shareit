package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select it " +
            "from Item as it " +
            "where (lower (it.name) like lower(?1) " +
            "or lower (it.description) like lower(?1)) " +
            "and it.available = true")
    List<Item> findByText(String text);

    List<Item> findAllByUserId(Long ownerId);

    List<Item> findAllByRequestId(Long requestId);
}
