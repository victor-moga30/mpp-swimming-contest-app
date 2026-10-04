package ro.mpp2026.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2026.model.User;
import ro.mpp2026.repository.UserRepository;
import ro.mpp2026.repository.hibernate.UserHibernateRepository;
import ro.mpp2026.rest.dto.ErrorResponse;
import ro.mpp2026.rest.dto.LoginRequest;
import ro.mpp2026.rest.dto.LoginResponse;
import ro.mpp2026.rest.security.JwtTokenUtil;
import ro.mpp2026.utils.PasswordUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = new UserHibernateRepository();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Username-ul nu poate fi gol."));
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Parola nu poate fi goala."));
        }

        User user = userRepository.findByUsername(request.getUsername().trim());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Username sau parola gresita."));
        }

        String passwordHash = PasswordUtils.hashPassword(request.getPassword());

        if (!passwordHash.equals(user.getPasswordHash())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Username sau parola gresita."));
        }

        String token = jwtTokenUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(
                new LoginResponse(token, user.getUsername(), user.getOffice())
        );
    }
}