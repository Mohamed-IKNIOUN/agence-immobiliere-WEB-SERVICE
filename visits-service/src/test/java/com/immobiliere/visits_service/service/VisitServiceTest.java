package com.immobiliere.visits_service.service;

import com.immobiliere.visits_service.dto.CreateVisitRequest;
import com.immobiliere.visits_service.dto.VisitResponse;
import com.immobiliere.visits_service.entity.Visit;
import com.immobiliere.visits_service.entity.VisitStatus;
import com.immobiliere.visits_service.exception.DuplicateVisitException;
import com.immobiliere.visits_service.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitService visitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. CAS NORMAL : Création réussie d'une visite
    @Test
    void testCreateVisit_NormalCase() {
        CreateVisitRequest request = new CreateVisitRequest();
        request.setAnnouncementId(1L);
        request.setClientId(5L);
        request.setDate(LocalDate.now().plusDays(3));
        request.setHeure("14:30");

        // Simuler qu'il n'y a pas de conflit
        when(visitRepository.existsConflictingVisit(any(), any(), any(), any())).thenReturn(false);
        when(visitRepository.save(any(Visit.class))).thenAnswer(invocation -> {
            Visit v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        VisitResponse response = visitService.createVisit(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(VisitStatus.DEMANDEE, response.getStatus());
        assertEquals("14:30", response.getHeure());
        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    // 2. CAS D'ERREUR : Tentative de création avec un conflit de réservation (Doublon)
    @Test
    void testCreateVisit_ErrorDuplicateConflict() {
        CreateVisitRequest request = new CreateVisitRequest();
        request.setAnnouncementId(1L);
        request.setClientId(5L);
        request.setDate(LocalDate.now().plusDays(3));
        request.setHeure("14:30");

        // Simuler la présence d'un conflit en base
        when(visitRepository.existsConflictingVisit(any(), any(), any(), any())).thenReturn(true);

        // Vérifier que l'exception personnalisée est bien levée
        assertThrows(DuplicateVisitException.class, () -> {
            visitService.createVisit(request);
        });

        // S'assurer que la méthode save n'a jamais été appelée
        verify(visitRepository, never()).save(any(Visit.class));
    }

    // 3. CAS LIMITE / NORMAL : Mise à jour valide du statut d'une visite
    @Test
    void testUpdateVisitStatus_NormalCase() {
        Long visitId = 1L;
        Visit existingVisit = Visit.builder()
                .id(visitId)
                .announcementId(1L)
                .clientId(5L)
                .date(LocalDate.now().plusDays(3))
                .heure(LocalTime.of(14, 30))
                .status(VisitStatus.DEMANDEE)
                .build();

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(existingVisit));
        when(visitRepository.save(any(Visit.class))).thenReturn(existingVisit);

        VisitResponse response = visitService.updateVisitStatus(visitId, VisitStatus.CONFIRMEE);

        assertNotNull(response);
        assertEquals(VisitStatus.CONFIRMEE, response.getStatus());
        verify(visitRepository, times(1)).save(existingVisit);
    }

    // 4. CAS D'ERREUR : Suppression ou action sur une visite inexistante
    @Test
    void testDeleteVisit_ErrorNotFound() {
        Long nonExistentId = 999L;
        when(visitRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            visitService.deleteVisit(nonExistentId);
        });

        verify(visitRepository, never()).delete(any(Visit.class));
    }
}