package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.service.TrackService;
import is.hi.hbv501g.web.dto.CreateTrackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;
    public TrackController(TrackService trackService) { this.trackService = trackService; }

    // GET /api/tracks?page=0&size=10
    @GetMapping
    public Page<Track> list(Pageable pageable) {
        return trackService.list(pageable);
    }

    // POST /api/tracks
    @PostMapping
    public ResponseEntity<Track> create(@RequestBody CreateTrackRequest req) {
        Track t = trackService.create(req.getTitle(), req.getArtist(), req.getAlbum(), req.getDurationMs());
        return ResponseEntity.created(URI.create("/api/tracks/" + t.getTrackId())).body(t);
    }
}
