package com.agence.favourites.service;

import com.agence.favourites.client.AnnouncementClient;
import com.agence.favourites.entity.Favori;
import com.agence.favourites.repository.FavoriRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriServiceTest {

    @Mock
    private FavoriRepository repository;

    @Mock
    private AnnouncementClient announcementClient;

    private FavoriService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new FavoriService(repository, announcementClient);
    }

    @Test
    void testAjouterFavori() {
        Favori favori = new Favori(1L, 10L);

        when(repository.save(any(Favori.class)))
            .thenReturn(favori);

        Favori result = service.ajouter(1L, 10L);

        assertEquals(1L, result.getClientId());
        assertEquals(10L, result.getAnnonceId());
        verify(repository).save(any(Favori.class));
    }

    @Test
    void testFavoriDejaExistant() {
        when(repository.existsByClientIdAndAnnonceId(1L, 10L))
            .thenReturn(true);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.ajouter(1L, 10L));

        assertEquals(409, exception.getStatusCode().value());

        verify(repository, never()).save(any(Favori.class));
    }

    @Test
    void testConsulterFavoris() {
        when(repository.findByClientId(1L))
            .thenReturn(List.of(new Favori(1L, 10L)));

        List<Favori> result = service.consulter(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getAnnonceId());
    }

    @Test
    void testSupprimerFavori() {
        Favori favori = new Favori(1L, 10L);

        when(repository.findByClientIdAndAnnonceId(1L, 10L))
            .thenReturn(Optional.of(favori));

        service.supprimer(1L, 10L);

        verify(repository).delete(favori);
    }

    @Test
    void testSupprimerFavoriInexistant() {
        when(repository.findByClientIdAndAnnonceId(1L, 10L))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.supprimer(1L, 10L));

        assertEquals(404, exception.getStatusCode().value());
    }
    @Test
    void testClientIdInvalide() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.ajouter(0L, 10L)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(repository, never()).save(any(Favori.class));
        verifyNoInteractions(announcementClient);
    }

    @Test
    void testAnnonceIdInvalide() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.ajouter(1L, -1L)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(repository, never()).save(any(Favori.class));
        verifyNoInteractions(announcementClient);
    }

    @Test
    void testAnnonceInexistante() {
        doThrow(new ResponseStatusException(
            org.springframework.http.HttpStatus.NOT_FOUND,
            "Annonce introuvable"
        )).when(announcementClient).verifierAnnonce(99L);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.ajouter(1L, 99L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(repository, never()).save(any(Favori.class));
    }
    @Test
    void testSupprimerAvecIdentifiantInvalide() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.supprimer(0L, 10L)
        );

        assertEquals(400, exception.getStatusCode().value());
        verifyNoInteractions(repository);
        verifyNoInteractions(announcementClient);
    }
}