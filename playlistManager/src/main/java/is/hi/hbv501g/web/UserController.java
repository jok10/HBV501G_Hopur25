package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.Playlist;
import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.repository.PlaylistRepository;
import is.hi.hbv501g.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository users;
    private final PlaylistRepository playlists;

    public UserController(UserRepository users, PlaylistRepository playlists) {
        this.users = users;
        this.playlists = playlists;
    }

    // Create user (simple, used mainly for debugging – for real use we’ll use /api/auth/register)
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = users.save(user);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getUserId())).body(saved);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = users.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get playlists for a given user
    @GetMapping("/{id}/playlists")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Long id) {
        Optional<User> userOpt = users.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Playlist> pls = playlists.findByOwner(userOpt.get());
        return ResponseEntity.ok(pls);
    }
}
