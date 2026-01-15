package webtech_semester_4.webtech.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5173", "https://webtech-s4-1.onrender.com"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    record RegisterReq(String email, String password) {}
    record LoginReq(String email, String password) {}
    record TokenRes(String token) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        if (req.email() == null || req.password() == null) return ResponseEntity.badRequest().body("missing data");
        if (repo.existsByEmail(req.email())) return ResponseEntity.status(409).body("email exists");

        AppUser u = new AppUser();
        u.setEmail(req.email());
        u.setPasswordHash(encoder.encode(req.password())); // Hash passwort
        repo.save(u);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        var userOpt = repo.findByEmail(req.email());
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("invalid credentials");

        AppUser u = userOpt.get();
        if (!encoder.matches(req.password(), u.getPasswordHash()))
            return ResponseEntity.status(401).body("invalid credentials");

        String fakeToken = "demo-" + u.getId();
        return ResponseEntity.ok(new TokenRes(fakeToken));
    }
}
