package kz.example.controller;

import jakarta.validation.Valid;
import kz.example.dto.AuthResponseDto;
import kz.example.dto.LoginRequestDto;
import kz.example.dto.RegistrationRequestDto;
import kz.example.dto.UserResponseDto;
import kz.example.dto.ForgotPasswordRequestDto; // New import
import kz.example.entity.User;
import kz.example.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        User newUser = authService.registerUser(registrationRequestDto);
        String jwtToken = authService.generateToken(newUser);
        AuthResponseDto authResponseDto = new AuthResponseDto(jwtToken, mapToUserResponseDto(newUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        User user = authService.loginUser(loginRequestDto);
        String jwtToken = authService.generateToken(user);
        AuthResponseDto authResponseDto = new AuthResponseDto(jwtToken, mapToUserResponseDto(user));
        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        authService.processForgotPassword(forgotPasswordRequestDto.getEmail());
        return ResponseEntity.ok("If an account with this email exists, a password reset link has been sent.");
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getDisplayName());
        dto.setBio(user.getBio());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLogin(user.getLastLogin());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}