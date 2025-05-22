package kz.example.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
    private Boolean isActive;
}