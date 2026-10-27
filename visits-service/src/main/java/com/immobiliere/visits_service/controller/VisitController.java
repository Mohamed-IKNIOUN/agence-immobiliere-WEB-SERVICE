package com.immobiliere.visits_service.controller;

import com.immobiliere.visits_service.dto.CreateVisitRequest;
import com.immobiliere.visits_service.dto.VisitResponse;
import com.immobiliere.visits_service.entity.VisitStatus;
import com.immobiliere.visits_service.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(@Valid @RequestBody CreateVisitRequest request){
        VisitResponse createdVisit = visitService.createVisit(request);
        return new ResponseEntity<>(createdVisit, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VisitResponse>> getAllVisits(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long announcementId
    ) {
        List<VisitResponse> visits = visitService.getAllVisits(clientId, announcementId);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getVisitById(@PathVariable Long id){
        VisitResponse visit = visitService.getVisitById(id);
        return ResponseEntity.ok(visit);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VisitResponse> updateVisitStatus(
            @PathVariable Long id,
            @RequestParam VisitStatus status
            ) {
        VisitResponse updatedVisit = visitService.updateVisitStatus(id, status);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id){
        visitService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }
}
