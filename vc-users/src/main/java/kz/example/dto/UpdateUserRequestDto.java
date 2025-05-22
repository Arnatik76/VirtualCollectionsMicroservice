package kz.example.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {

    @Size(max = 100, message = "The display name must not exceed 100 characters.")
    private String displayName;

    @Size(max = 1000, message = "The bio must not exceed 1000 characters.")
    private String bio;

    @Size(max = 255, message = "The avatar URL must not exceed 255 characters.")
    private String avatarUrl;
}