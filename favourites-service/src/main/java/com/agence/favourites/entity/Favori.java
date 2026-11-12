package com.agence.favourites.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "favoris",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"client_id", "annonce_id"}
    )
)
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "annonce_id", nullable = false)
    private Long annonceId;

    public Favori() {
    }

    public Favori(Long clientId, Long annonceId) {
        this.clientId = clientId;
        this.annonceId = annonceId;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }
}
