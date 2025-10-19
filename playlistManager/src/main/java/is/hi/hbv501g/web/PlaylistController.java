package is.hi.hbv501g.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.service.PlaylistService;


@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) { this.playlistService = playlistService; }

    // Data transfer object for POST
    public static class CreatePlaylistRequest {
        public String name;
        public boolean isPublic;
        public String imageUrl;
    }

    // Runs when a POST request is sent to /api/playlists/
    // Creates a new playlist using data from the request body
    @PostMapping
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistRequest request) {
        if (request == null || request.name == null || request.name.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Playlist playlist = playlistService.create(request.name, request.isPublic, request.imageUrl);

        return ResponseEntity.ok(playlist);
    }

    // Handles GET requests to /api/playlists/{id}
    // Fetches a playlist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> get(@PathVariable Long id) {
        Optional<Playlist> optional = playlistService.get(id);

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Fetches and lists all playlists (paginated)
    @GetMapping
    public Page<Playlist> list(@PageableDefault(size = 10) Pageable pageable) {         // 10 = default page size
        return playlistService.list(pageable);
    }

    // Handles DELETE requests to /api/playlists/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = playlistService.delete(id);

        if (deleted) {
            return ResponseEntity.noContent().build();          // 204
        } else {
            return ResponseEntity.notFound().build();           // 404
        }
    }
}
