package com.immobiliere.visits_service.repository;

import com.immobiliere.visits_service.entity.Visit;
import com.immobiliere.visits_service.entity.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByClientId(Long clientId);

    List<Visit> findByAnnouncementId(Long announcementID);

    List<Visit> findByStatus(VisitStatus status);

    boolean existsByAnnouncementIdAndVisitDateAndStatusNot(
            Long announcementId,
            LocalDateTime visitDate,
            VisitStatus status
    );
}
