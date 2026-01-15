package webtech_semester_4.webtech.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService_Mock {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService_Mock(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public AppUser register(String email, String rawPassword) {
        AppUser u = new AppUser();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPassword));
        return repo.save(u);
    }
}
