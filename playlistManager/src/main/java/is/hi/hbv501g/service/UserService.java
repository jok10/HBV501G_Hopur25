package is.hi.hbv501g.service;

import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // Must be at least 256 bits (32+ chars) for HS256
    private static final String JWT_SECRET = "this_is_a_super_super_secret_key_12345";
    private final Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public User register(User user) {
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        return userRepo.save(user);
    }

    /**
     * Returns JWT if username/password OK, otherwise null.
     */
    public String authenticate(String username, String rawPassword) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isPresent() && encoder.matches(rawPassword, userOpt.get().getPasswordHash())) {
            Date now = new Date();
            Date exp = new Date(System.currentTimeMillis() + 86400000); // 1 day
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(exp)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    // >>> ADD THIS <<<
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public boolean passwordMatches(String raw, User user) {
        return encoder.matches(raw, user.getPasswordHash());
    }

}
