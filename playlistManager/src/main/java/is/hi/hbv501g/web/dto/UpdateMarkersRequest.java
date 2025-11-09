package is.hi.hbv501g.web.dto;

public class UpdateMarkersRequest {
    private Long startMs;
    private Long endMs;

    public Long getStartMs() { return startMs; }
    public Long getEndMs() { return endMs; }

    public void setStartMs(Long startMs) { this.startMs = startMs; }
    public void setEndMs(Long endMs) { this.endMs = endMs; }
}
