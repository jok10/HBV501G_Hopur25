package is.hi.hbv501g.domain;

import jakarta.persistence.*;

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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;


    public Playlist() {}

    // Getters
    public User getOwner() { return owner; }
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
    public void setOwner(User owner) { this.owner = owner; }
}
