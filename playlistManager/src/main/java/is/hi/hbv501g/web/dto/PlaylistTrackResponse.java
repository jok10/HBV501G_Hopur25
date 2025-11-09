package is.hi.hbv501g.web.dto;

import is.hi.hbv501g.domain.Track;


public class PlaylistTrackResponse {
    private Long playlistTrackId;
    private Long playlistId;
    private Long trackId;
    private int position;
    private long startMs;
    private long endMs;

    private Track track;

    public Long getPlaylistTrackId() { return playlistTrackId; }
    public void setPlaylistTrackId(Long playlistTrackId) { this.playlistTrackId = playlistTrackId; }
    public Long getPlaylistId() { return playlistId; }
    public void setPlaylistId(Long playlistId) { this.playlistId = playlistId; }
    public Long getTrackId() { return trackId; }
    public void setTrackId(Long trackId) { this.trackId = trackId; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public long getStartMs() { return startMs; }
    public void setStartMs(long startMs) { this.startMs = startMs; }
    public long getEndMs() { return endMs; }
    public void setEndMs(long endMs) { this.endMs = endMs; }
}
