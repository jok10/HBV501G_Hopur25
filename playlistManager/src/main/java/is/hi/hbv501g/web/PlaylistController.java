package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) { this.playlistService = playlistService; }

    // Data transfer object
    public static class CreatePlaylistRequest {
        public String name;
        public boolean isPublic;
        public String imageUrl;
    }

    // Runs when a POST request is sent to /api/playlists/{id}
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
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
