package is.hi.hbv501g.web.dto;

public class AddTrackRequest {
    private Long playlistId;
    private Long trackId;

    public Long getPlaylistId() { return playlistId; }
    public void setPlaylistId(Long playlistId) { this.playlistId = playlistId; }

    public Long getTrackId() { return trackId; }
    public void setTrackId(Long trackId) { this.trackId = trackId; }
}
