package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select r " +
            "from ItemRequest as r " +
            "order by r.created desc")
    List<ItemRequest> findOrderByCreated();

    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(Long ownerId);
}
