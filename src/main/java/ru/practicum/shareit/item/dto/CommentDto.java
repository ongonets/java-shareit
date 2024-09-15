package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentDto {
    private long id;
    private String authorName;
    private String text;
    private Instant created;
}
