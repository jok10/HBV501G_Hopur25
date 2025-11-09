package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.domain.PlaylistTrack;
import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.repository.UserRepository;
import is.hi.hbv501g.service.PlaylistService;
import is.hi.hbv501g.web.dto.PlaylistTrackResponse;
import is.hi.hbv501g.web.dto.UpdateMarkersRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private Long userId;
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // CREATE a playlist
    @PostMapping
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistRequest req) {
        Optional<User> userOpt = UserRepository.findById(req.getUserId());
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().build(); // or custom error
        }
        Playlist p = playlistService.create(req.getName(), req.isPublic(), req.getImageUrl());
        return ResponseEntity.created(URI.create("/api/playlists/" + p.getPlaylistId())).body(p);
    }

    // GET paginated list of playlists
    @GetMapping
    public Page<Playlist> list(Pageable pageable) {
        return playlistService.list(pageable);
    }

    // GET a specific playlist by ID
    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> get(@PathVariable Long playlistId) {
        return playlistService.get(playlistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a playlist
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> delete(@PathVariable Long playlistId) {
        boolean deleted = playlistService.deletePlaylist(playlistId);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // POST a track to a playlist
    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<PlaylistTrackResponse> addTrack(@PathVariable Long playlistId,
                                                          @RequestParam Long trackId) {
        PlaylistTrack pt = playlistService.addTrack(playlistId, trackId);

        PlaylistTrackResponse dto = new PlaylistTrackResponse();
        dto.setPlaylistTrackId(pt.getPlaylistTrackId());
        dto.setPlaylistId(playlistId);
        dto.setTrackId(trackId);
        dto.setPosition(pt.getPosition());
        dto.setStartMs(pt.getStartMs());
        dto.setEndMs(pt.getEndMs());

        return ResponseEntity
                .created(URI.create("/api/playlists/" + playlistId + "/tracks/" + pt.getPlaylistTrackId()))
                .body(dto);
    }

    // GET tracks in a playlist
    @GetMapping("/{playlistId}/tracks")
    public List<PlaylistTrackResponse> listTracks(@PathVariable Long playlistId) {
        return playlistService.listTracksDTO(playlistId);
    }

    // DELETE a track from a playlist
    @DeleteMapping("/{playlistId}/tracks/{playlistTrackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long playlistId, @PathVariable Long playlistTrackId) {
        boolean deleted = playlistService.deleteTrack(playlistId, playlistTrackId);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // UPDATE markers for a track in a playlist
    @PutMapping("/{playlistId}/tracks/{playlistTrackId}/markers")
    public ResponseEntity<PlaylistTrackResponse> updateMarkers(
            @PathVariable Long playlistId,
            @PathVariable Long playlistTrackId,
            @RequestBody UpdateMarkersRequest request) {
        try {
            PlaylistTrackResponse dto = playlistService.updateTrackMarkers(playlistId, playlistTrackId, request.getStartMs(), request.getEndMs());
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET shareable link to public playlist
    @GetMapping("/{playlistId}/share")
    public ResponseEntity<String> sharePlaylist(@PathVariable Long playlistId) {
        Optional<Playlist> optional = playlistService.get(playlistId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Playlist playlist = optional.get();
        if (!playlist.isPublic()) {
            return ResponseEntity.status(403).body("This playlist is private.");
        }
        String playlistLink = "http://localhost:8080/api/playlists/" + playlistId;
        return ResponseEntity.ok(playlistLink);
    }

    // COPY a playlist
    @PostMapping("/{playlistId}/copy")
    public ResponseEntity<Playlist> copyPlaylist(@PathVariable Long playlistId) {
        try {
            Playlist copiedPlaylist = playlistService.copyPlaylist(playlistId);
            return ResponseEntity.created(URI.create("/api/playlists/" + copiedPlaylist.getPlaylistId()))
                    .body(copiedPlaylist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();
        }
    }

    // DTO for creating a playlist
    public static class CreatePlaylistRequest {
        private String name;
        private boolean isPublic;
        private String imageUrl;

        public String getName() {
            return name;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPublic(boolean aPublic) {
            isPublic = aPublic;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
