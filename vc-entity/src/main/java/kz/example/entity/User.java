package kz.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_password", nullable = false)
    private String password;

    @Size(max = 100)
    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "bio", length = Integer.MAX_VALUE)
    private String bio;

    @Size(max = 255)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

}