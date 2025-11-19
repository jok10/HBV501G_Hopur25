package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        users.register(user);
        return ResponseEntity.created(URI.create("/api/users/" + user.getUserId()))
                .body(Map.of("status", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String token = users.authenticate(body.get("username"), body.get("password"));
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }
}
