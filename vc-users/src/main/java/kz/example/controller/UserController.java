package kz.example.controller;

import jakarta.validation.Valid;
import kz.example.dto.UpdateUserRequestDto;
import kz.example.dto.UserResponseDto;
import kz.example.service.UserService;
import kz.example.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return ResponseEntity.ok(userService.getUserById(currentUserId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateCurrentUser(@Valid @RequestBody UpdateUserRequestDto updateUserDto) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return ResponseEntity.ok(userService.updateUser(currentUserId, updateUserDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<UserResponseDto>> getUsersByIds(@RequestBody List<Long> userIds) {
        return ResponseEntity.ok(userService.getUsersByIds(userIds));
    }

    @PostMapping("/{userIdToFollow}/follow")
    public ResponseEntity<Void> followUser(@PathVariable Long userIdToFollow) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        userService.followUser(currentUserId, userIdToFollow);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{userIdToUnfollow}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userIdToUnfollow) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        userService.unfollowUser(currentUserId, userIdToUnfollow);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<UserResponseDto>> getFollowers(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getFollowers(userId, pageable));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<Page<UserResponseDto>> getFollowing(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getFollowing(userId, pageable));
    }
}