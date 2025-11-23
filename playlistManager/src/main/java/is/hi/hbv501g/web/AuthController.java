package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    // -------------- REGISTER (ALSO LOGS IN) --------------
    public static class RegisterRequest {
        public String username;
        public String email;
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req, HttpSession session) {
        if (req.username == null || req.password == null || req.username.isBlank() || req.password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username + password required"));
        }

        User u = new User();
        u.setUsername(req.username);
        u.setEmail(req.email);
        u.setPasswordHash(req.password); // UserService hashes it
        User saved = users.register(u);

        // store session login
        session.setAttribute("userId", saved.getUserId());

        return ResponseEntity.created(URI.create("/api/users/" + saved.getUserId()))
                .body(Map.of("userId", saved.getUserId(), "username", saved.getUsername()));
    }

    // -------------- LOGIN (SESSION ONLY) --------------
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpSession session) {
        if (req.username == null || req.password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username + password required"));
        }

        Optional<User> userOpt = users.findByUsername(req.username);
        if (userOpt.isEmpty() || !users.passwordMatches(req.password, userOpt.get())) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }

        User user = userOpt.get();
        session.setAttribute("userId", user.getUserId());

        return ResponseEntity.ok(Map.of(
                "loggedIn", true,
                "userId", user.getUserId(),
                "username", user.getUsername()
        ));
    }

    // -------------- LOGOUT --------------
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    // -------------- WHO AM I --------------
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Long uid = (Long) session.getAttribute("userId");
        if (uid == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }
        return ResponseEntity.ok(Map.of("authenticated", true, "userId", uid));
    }
}
