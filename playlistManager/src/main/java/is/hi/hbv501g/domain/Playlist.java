package is.hi.hbv501g.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;


@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;
    private String name;
    private boolean isPublic;
    private String imageUrl;
    private Instant createdAt;

    public Playlist() {}

    // Getters
    public Long getPlaylistId() { return playlistId; }
    public String getName() { return name; }
    public boolean isPublic() { return isPublic; }
    public String getImageUrl() { return imageUrl; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setPlaylistId(Long playlistId) { this.playlistId = playlistId; }
    public void setName(String name) { this.name = name; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
