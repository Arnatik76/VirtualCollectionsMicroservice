package kz.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequestDto {

    @NotBlank(message = "The user name cannot be empty.")
    @Size(max = 50, message = "The user's name must not exceed 50 characters.")
    private String username;

    @NotBlank(message = "An email cannot be empty")
    @Email(message = "Incorrect email format")
    @Size(max = 255, message = "The email must not exceed 255 characters.")
    private String email;

    @NotBlank(message = "The password cannot be empty.")
    private String password;

    @Size(max = 100, message = "The display name must not exceed 100 characters.")
    private String displayName;

}
