package com.immobiliere.visits_service.service;

import com.immobiliere.visits_service.dto.CreateVisitRequest;
import com.immobiliere.visits_service.dto.VisitResponse;
import com.immobiliere.visits_service.entity.Visit;
import com.immobiliere.visits_service.entity.VisitStatus;
import com.immobiliere.visits_service.exception.DuplicateVisitException;
import com.immobiliere.visits_service.exception.ResourceNotFoundException;
import com.immobiliere.visits_service.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;

    @Transactional
    public VisitResponse createVisit(CreateVisitRequest request) {
        LocalTime visitTime = LocalTime.parse(request.getHeure());

        boolean isConflict = visitRepository.existsConflictingVisit(
                request.getAnnouncementId(),
                request.getDate(),
                visitTime,
                List.of(VisitStatus.ANNULEE, VisitStatus.REFUSEE)
        );

        if (isConflict) {
            throw new DuplicateVisitException("A visit is already scheduled for this property at this date and time.");
        }

        Visit visit = Visit.builder()
                .announcementId(request.getAnnouncementId())
                .clientId(request.getClientId())
                .date(request.getDate())
                .heure(visitTime)
                .status(VisitStatus.DEMANDEE)
                .build();

        Visit savedVisit = visitRepository.save(visit);
        return mapToResponse(savedVisit);
    }

    public List<VisitResponse> getAllVisits(Long clientId, Long announcementId) {
        List<Visit> visits;
        if (clientId != null) {
            visits = visitRepository.findByClientId(clientId);
        } else if (announcementId != null) {
            visits = visitRepository.findByAnnouncementId(announcementId);
        } else {
            visits = visitRepository.findAll();
        }

        return visits.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VisitResponse getVisitById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + id));
        return mapToResponse(visit);
    }

    @Transactional
    public VisitResponse updateVisitStatus(Long id, VisitStatus status) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + id));
        switch (status) {
            case CONFIRMEE -> visit.confirmer();
            case ANNULEE -> visit.annuler();
            default -> visit.setStatus(status); // Pour REFUSEE ou autres
        }
        Visit updatedVisit = visitRepository.save(visit);
        return mapToResponse(updatedVisit);
    }

    @Transactional
    public void deleteVisit(Long id) {
        if (!visitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visit not found with id: " + id);
        }
        visitRepository.deleteById(id);
    }

    private VisitResponse mapToResponse(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .announcementId(visit.getAnnouncementId())
                .clientId(visit.getClientId())
                .agentId(visit.getAgentId())
                .date(visit.getDate())
                .heure(String.valueOf(visit.getHeure()))
                .status(visit.getStatus())
                .createdAt(visit.getCreatedAt())
                .build();
    }
}
