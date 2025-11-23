package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.Track;
import is.hi.hbv501g.repository.TrackRepository;
import is.hi.hbv501g.service.TrackService;
import is.hi.hbv501g.web.dto.CreateTrackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpSession;


import java.net.URI;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackRepository trackRepository;
    private final TrackService trackService;
    public TrackController(TrackService trackService, TrackRepository trackRepository) {
        this.trackService = trackService;
        this.trackRepository = trackRepository;
    }

    // GET /api/tracks?page=0&size=10
    @GetMapping
    public Page<Track> list(Pageable pageable) {
        return trackService.list(pageable);
    }

    // POST /api/tracks
    @PostMapping
    public ResponseEntity<Track> create(@RequestBody CreateTrackRequest req, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Track t = trackService.create(
                req.getTitle(),
                req.getArtist(),
                req.getAlbum(),
                req.getDurationMs()
        );
        t.setFileUrl(req.getFileUrl());
        t.setMimeType(req.getMimeType());

        t = trackRepository.save(t);

        return ResponseEntity
                .created(URI.create("/api/tracks/" + t.getTrackId()))
                .body(t);
    }


    @GetMapping("/{id}/stream")
    public ResponseEntity<Void> streamTrack(@PathVariable Long id) {
        var track = trackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found"));
        if (track.getFileUrl() == null || track.getFileUrl().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No audio file for this track");
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(track.getFileUrl()))
                .build();
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long trackId) {
        if (trackService.delete(trackId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
