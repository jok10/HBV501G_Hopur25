package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.domain.PlaylistTrack;
import is.hi.hbv501g.service.PlaylistService;
import is.hi.hbv501g.web.dto.PlaylistTrackResponse;
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

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // CREATE a playlist
    @PostMapping
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistRequest req) {
        Playlist p = playlistService.create(req.getName(), req.isPublic(), req.getImageUrl());
        return ResponseEntity.created(URI.create("/api/playlists/" + p.getPlaylistId())).body(p);
    }

    @GetMapping
    public Page<Playlist> list(Pageable pageable) {
        return playlistService.list(pageable);
    }

    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<PlaylistTrackResponse> addTrack(@PathVariable Long playlistId,
                                                          @RequestParam Long trackId) {
        PlaylistTrack pt = playlistService.addTrack(playlistId, trackId);

        PlaylistTrackResponse dto = new PlaylistTrackResponse();
        dto.setPlaylistTrackId(pt.getPlaylistTrackId());
        dto.setPlaylistId(playlistId); // we already know it
        dto.setTrackId(trackId);       // we already know it
        dto.setPosition(pt.getPosition());
        dto.setStartMs(pt.getStartMs());
        dto.setEndMs(pt.getEndMs());

        return ResponseEntity
                .created(URI.create("/api/playlists/" + playlistId + "/tracks/" + pt.getPlaylistTrackId()))
                .body(dto);
    }

    @GetMapping("/{playlistId}/tracks")
    public List<PlaylistTrackResponse> listTracks(@PathVariable Long playlistId) {
        return playlistService.listTracksDTO(playlistId);
    }

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

    // DELETE /api/playlists/{playlistId}
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        boolean deleted = playlistService.deletePlaylist(playlistId);

        if (deleted) {
            return ResponseEntity.noContent().build();          // 204
        } else {
            return ResponseEntity.notFound().build();           // 404
        }
    }

    // DELETE /api/playlists/{playlistId}/tracks/{playlistTrackId}
    @DeleteMapping("/{playlistId}/tracks/{playlistTrackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long playlistId, @PathVariable Long playlistTrackId) {
        boolean deleted = playlistService.deleteTrack(playlistTrackId);

        if (deleted) {
            return ResponseEntity.noContent().build();          // 204
        } else {
            return ResponseEntity.notFound().build();           // 404
        }
    }

    // GET /api/playlists/{playlistId}/share
    @GetMapping("/{playlistId}/share")
    public ResponseEntity<String> sharePlaylist(@PathVariable Long playlistId) {
        Optional<Playlist> optional = playlistService.get(playlistId);

        // If not found
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Playlist playlist = optional.get();

        // If private
        if (!playlist.isPublic()) {
            return ResponseEntity.status(403).body("This playlist is private.");
        }

        // If public
        String playlistLink = "http://localhost:8080/api/playlists/" + playlistId;

        return ResponseEntity.ok(playlistLink);
    }

    // POST /api/playlists/{playlistId}/copy
    @PostMapping("/{playlistId}/copy")
    public ResponseEntity<Playlist> copyPlaylist(@PathVariable Long playlistId) {
        try {
            Playlist copiedPlaylist = playlistService.copyPlaylist(playlistId);

            return ResponseEntity.created(URI.create("/api/playlists/" + copiedPlaylist.getPlaylistId())).body(copiedPlaylist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();           // playlist not found
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();          // private playlist
        }
    }
}