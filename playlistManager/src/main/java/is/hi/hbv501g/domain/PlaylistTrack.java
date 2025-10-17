package is.hi.hbv501g.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "playlist_tracks")
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistTrackId;
    private int position;
    private long startMs;
    private long endMs;

    // Getters
    public Long getPlaylistTrackId() { return playlistTrackId; }
    public int getPosition() { return position; }
    public long getStartMs() { return startMs; }
    public long getEndMs() { return endMs; }

    // Setters
    public void setPlaylistTrackId(Long playlistTrackId) { this.playlistTrackId = playlistTrackId; }
    public void setPosition(int position) { this.position = position; }
    public void setStartMs(long startMs) { this.startMs = startMs; }
    public void setEndMs(long endMs) { this.endMs = endMs; }
}
