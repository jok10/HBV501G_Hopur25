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

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getUserId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/playlists")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Playlist> playlists = PlaylistRepository.findByOwner(userOpt.get());
        return ResponseEntity.ok(playlists);
    }

}
