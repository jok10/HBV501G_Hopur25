package is.hi.hbv501g.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "tracks")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;
    private String title;
    private String artist;
    private String album;
    private long durationMs;

    //Url for actual audio file
    private String fileUrl;
    private String mimeType; //mpeg,ogg,...

    public Track() {}

    // Getters
    public Long getTrackId() { return trackId; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public long getDurationMs() { return durationMs; }
    public String getFileUrl() { return fileUrl; }
    public String getMimeType() { return mimeType; }

    // Setters
    public void setTrackId(Long trackId) { this.trackId = trackId; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) {this.album = album; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}