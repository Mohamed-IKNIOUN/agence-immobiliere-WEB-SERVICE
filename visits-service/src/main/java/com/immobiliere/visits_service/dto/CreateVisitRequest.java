package com.immobiliere.visits_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateVisitRequest {
    @NotNull(message = "Announcement ID is required")
    private Long announcementId;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Visit date is required")
    @Future(message = "Visit date must be in the future")
    private LocalDateTime visitDate;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
