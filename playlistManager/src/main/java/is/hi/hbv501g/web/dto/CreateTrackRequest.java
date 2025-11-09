package is.hi.hbv501g.web.dto;

public class CreateTrackRequest {
    private String title;
    private String artist;
    private String album;
    private Long durationMs;
    private String fileUrl;
    private String mimeType;

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public long getDurationMs() { return durationMs; }
    public String getFileUrl() { return fileUrl; }
    public String getMimeType() { return mimeType; }

    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}
