package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewBookingRequestDto {
    @NotNull(message = "ItemId was entered incorrectly")
    long itemId;
    @FutureOrPresent(message = "Start was entered incorrectly")
    LocalDateTime start;
    @Future(message = "End was entered incorrectly")
    LocalDateTime end;
}