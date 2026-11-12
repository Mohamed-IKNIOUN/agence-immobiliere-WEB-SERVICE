package com.agence.favourites.service;

import com.agence.favourites.client.AnnouncementClient;
import com.agence.favourites.entity.Favori;
import com.agence.favourites.repository.FavoriRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FavoriService {

    private final FavoriRepository repository;
    private final AnnouncementClient announcementClient;

    public FavoriService(
            FavoriRepository repository,
            AnnouncementClient announcementClient) {
        this.repository = repository;
        this.announcementClient = announcementClient;
    }

    // Ajouter une annonce aux favoris
    public Favori ajouter(Long clientId, Long annonceId) {

        if (clientId == null || clientId <= 0 ||
            annonceId == null || annonceId <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Identifiants invalides"
            );
        }

        if (repository.existsByClientIdAndAnnonceId(
                clientId, annonceId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cette annonce est deja dans les favoris"
            );
        }

        // Verification aupres du microservice Annonces
        announcementClient.verifierAnnonce(annonceId);

        return repository.save(
            new Favori(clientId, annonceId)
        );
    }

    // Consulter les favoris d'un client
    public List<Favori> consulter(Long clientId) {
        if (clientId == null || clientId <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Identifiant client invalide"
            );
        }
        return repository.findByClientId(clientId);
    }

    // Supprimer une annonce des favoris
    public void supprimer(Long clientId, Long annonceId) {

        if (clientId == null || clientId <= 0 ||
            annonceId == null || annonceId <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Identifiants invalides"
            );
        }

        Favori favori = repository
            .findByClientIdAndAnnonceId(clientId, annonceId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Favori introuvable"
                )
            );

        repository.delete(favori);
    }
}