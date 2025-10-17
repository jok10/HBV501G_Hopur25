package is.hi.hbv501g.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;


@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;
    private Instant createdAt;

    public Favorite() {}

    // Getters
    public Long getFavoriteId() { return favoriteId; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setFavoriteId(Long favoriteId) { this.favoriteId = favoriteId; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
