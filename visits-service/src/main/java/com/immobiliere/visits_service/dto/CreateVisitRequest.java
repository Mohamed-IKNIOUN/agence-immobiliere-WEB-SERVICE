package com.immobiliere.visits_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateVisitRequest {
    @NotNull(message = "Announcement ID is required")
    private Long announcementId;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Visit date is required")
    @Future(message = "Visit date must be in the future")
    private LocalDate date;

    @NotBlank(message = "Visit time is required")
    private String heure;
}
