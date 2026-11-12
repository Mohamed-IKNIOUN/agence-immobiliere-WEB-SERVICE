package com.agence.favourites.repository;

import com.agence.favourites.entity.Favori;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriRepository
        extends JpaRepository<Favori, Long> {

    List<Favori> findByClientId(Long clientId);

    boolean existsByClientIdAndAnnonceId(
        Long clientId,
        Long annonceId
    );

    Optional<Favori> findByClientIdAndAnnonceId(
        Long clientId,
        Long annonceId
    );
}