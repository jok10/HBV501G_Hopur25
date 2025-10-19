package is.hi.hbv501g.web.dto;

public class CreateTrackRequest {
    private String title;
    private String artist;
    private String album;
    private Long durationMs;

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public Long getDurationMs() { return durationMs; }

    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
}
