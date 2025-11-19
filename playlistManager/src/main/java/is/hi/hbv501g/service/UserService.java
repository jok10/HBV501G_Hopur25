package is.hi.hbv501g.service;

import is.hi.hbv501g.domain.User;
import is.hi.hbv501g.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.userRepo = repo;
        this.encoder = encoder;
    }

    // Register new user (password is raw in user.getPasswordHash())
    public User register(User user) {
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        return userRepo.save(user);
    }

    // Return user if credentials are valid, otherwise null
    public User validateCredentials(String username, String rawPassword) {
        return userRepo.findByUsername(username)
                .filter(u -> encoder.matches(rawPassword, u.getPasswordHash()))
                .orElse(null);
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }
}
