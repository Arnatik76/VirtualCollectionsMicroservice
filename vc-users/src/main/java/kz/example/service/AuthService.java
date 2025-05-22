package kz.example.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kz.example.dto.LoginRequestDto;
import kz.example.dto.RegistrationRequestDto;
import kz.example.entity.User;
import kz.example.exception.ResourceAlreadyExistsException;
import kz.example.exception.ResourceNotFoundException;
import kz.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class); // New logger
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.isEmpty() || jwtSecret.length() < 32) {
            logger.warn("WARNING: jwt.secret is not set or too short in application.properties. Using a default (less secure) or potentially failing.");
        }
        try {
            this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            logger.error("Error initializing JWT signing key", e);
            throw new RuntimeException("Error initializing JWT signing key", e);
        }
    }

    @Transactional
    public User registerUser(RegistrationRequestDto registrationRequestDto) {
        String username = registrationRequestDto.getUsername();
        String email = registrationRequestDto.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("Username '" + username + "' already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email '" + email + "' already exists");
        }

        User newUser = new User();
        newUser.setUsername(registrationRequestDto.getUsername());
        newUser.setEmail(registrationRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        newUser.setDisplayName(registrationRequestDto.getDisplayName());
        newUser.setCreatedAt(OffsetDateTime.now());
        newUser.setIsActive(true);

        return userRepository.save(newUser);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Transactional
    public User loginUser(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        user.setLastLogin(OffsetDateTime.now());
        userRepository.save(user);

        return user;
    }

    @Transactional
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            String resetToken = UUID.randomUUID().toString();
            logger.info("Password reset token generated for user: {}", email);
            logger.info("Simulating password reset email sent to {} with token {}", email, resetToken);
        } else {
            logger.info("Password reset request for non-existent or inactive email: {}", email);
        }
    }
}