package com.immobiliere.visits_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_id", nullable = false)
    private Long announcementId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "visit_date", nullable = false)
    private LocalDate date;

    @Column(name = "visit_heure", nullable = false)
    private LocalTime heure;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VisitStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void confirmer() {
        this.status = VisitStatus.CONFIRMEE;
    }

    public void annuler() {
        this.status = VisitStatus.ANNULEE;
    }
}
