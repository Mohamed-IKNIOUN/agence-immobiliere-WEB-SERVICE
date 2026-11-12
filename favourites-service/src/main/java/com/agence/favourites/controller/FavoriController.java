package com.agence.favourites.controller;

import com.agence.favourites.entity.Favori;
import com.agence.favourites.service.FavoriService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoris")
public class FavoriController {

    private final FavoriService service;

    public FavoriController(FavoriService service) {
        this.service = service;
    }

    // Ajouter un favori
    @PostMapping("/{clientId}/{annonceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Favori ajouter(
            @PathVariable Long clientId,
            @PathVariable Long annonceId) {

        return service.ajouter(clientId, annonceId);
    }

    // Consulter les favoris
    @GetMapping("/client/{clientId}")
    public List<Favori> consulter(
            @PathVariable Long clientId) {

        return service.consulter(clientId);
    }

    // Supprimer un favori
    @DeleteMapping("/{clientId}/{annonceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(
            @PathVariable Long clientId,
            @PathVariable Long annonceId) {

        service.supprimer(clientId, annonceId);
    }
}