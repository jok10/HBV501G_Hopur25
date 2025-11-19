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

    // ---------- DTOs ----------
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }

        public void setUsername(String username) { this.username = username; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public String getPassword() { return password; }

        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
    }

    // ---------- REGISTER ----------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req, HttpSession session) {
        // basic sanity check, you can make this stricter
        if (req.getUsername() == null || req.getUsername().isBlank() ||
                req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        // IMPORTANT: we use the password field and let UserService hash it
        u.setPasswordHash(req.getPassword());

        User saved = users.register(u);

        // log the user in via session
        session.setAttribute("userId", saved.getUserId());

        return ResponseEntity
                .created(URI.create("/api/users/" + saved.getUserId()))
                .body(Map.of(
                        "userId", saved.getUserId(),
                        "username", saved.getUsername()
                ));
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpSession session) {
        String token = users.authenticate(req.getUsername(), req.getPassword());
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        Optional<User> userOpt = users.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) {
            // Should not happen if authenticate() succeeded, but just in case
            return ResponseEntity.status(500).body(Map.of("error", "User not found after authentication"));
        }

        User user = userOpt.get();

        // Persist login in session
        session.setAttribute("userId", user.getUserId());

        // We *also* return the JWT if you want to use it later
        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getUserId(),
                "username", user.getUsername()
        ));
    }

    // ---------- LOGOUT ----------
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    // ---------- WHO AM I ----------
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }
        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "userId", userId
        ));
    }
}
