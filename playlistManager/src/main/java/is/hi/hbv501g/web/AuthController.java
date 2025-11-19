package is.hi.hbv501g.web;

import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    // ------- DTOs -------

    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private String token;
        private Long userId;
        private String username;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    // ------- Register -------

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        // temporarily store raw password in passwordHash field – UserService will encode it
        u.setPasswordHash(req.getPassword());

        User saved = users.register(u);
        return ResponseEntity
                .created(URI.create("/api/users/" + saved.getUserId()))
                .body(saved);
    }

    // ------- Login -------

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req, HttpSession session) {
        String jwt = users.authenticate(req.getUsername(), req.getPassword());
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOpt = users.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOpt.get();

        // store user id in HTTP session (used by PlaylistController)
        session.setAttribute("userId", user.getUserId());

        LoginResponse resp = new LoginResponse();
        resp.setToken(jwt);
        resp.setUserId(user.getUserId());
        resp.setUsername(user.getUsername());

        return ResponseEntity.ok(resp);
    }

    // ------- Logout -------

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
