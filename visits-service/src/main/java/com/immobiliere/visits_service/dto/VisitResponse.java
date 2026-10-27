package com.immobiliere.visits_service.dto;

import com.immobiliere.visits_service.entity.VisitStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VisitResponse {
    private Long id;
    private Long announcementId;
    private Long clientId;
    private Long agentId;
    private LocalDateTime visitDate;
    private VisitStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
