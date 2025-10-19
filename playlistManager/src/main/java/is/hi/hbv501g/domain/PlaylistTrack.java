package is.hi.hbv501g.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist_tracks")
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistTrackId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    private int position;
    private long startMs;
    private long endMs;

    // Getters
    public Long getPlaylistTrackId() { return playlistTrackId; }
    public Playlist getPlaylist() { return playlist; }
    public Track getTrack() { return track; }
    public int getPosition() { return position; }
    public long getStartMs() { return startMs; }
    public long getEndMs() { return endMs; }

    // Setters
    public void setPlaylistTrackId(Long playlistTrackId) { this.playlistTrackId = playlistTrackId; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }
    public void setTrack(Track track) { this.track = track; }
    public void setPosition(int position) { this.position = position; }
    public void setStartMs(long startMs) { this.startMs = startMs; }
    public void setEndMs(long endMs) { this.endMs = endMs; }
}
