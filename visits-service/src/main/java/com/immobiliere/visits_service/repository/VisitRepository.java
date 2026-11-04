package com.immobiliere.visits_service.repository;

import com.immobiliere.visits_service.entity.Visit;
import com.immobiliere.visits_service.entity.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByClientId(Long clientId);

    List<Visit> findByAnnouncementId(Long announcementID);

    List<Visit> findByStatus(VisitStatus status);

    @Query("SELECT COUNT(v) > 0 FROM Visit v WHERE v.announcementId = :announcementId AND v.date = :date AND v.heure = :heure AND v.status NOT IN :statuses")
    boolean existsConflictingVisit(
            @Param("announcementId") Long announcementId,
            @Param("date") LocalDate date,
            @Param("heure") LocalTime heure,
            @Param("statuses") List<VisitStatus> statuses
    );
}
