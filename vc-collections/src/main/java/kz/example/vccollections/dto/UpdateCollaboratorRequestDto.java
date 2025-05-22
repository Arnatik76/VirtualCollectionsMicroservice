package kz.example.vccollections.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCollaboratorRequestDto {
    @NotBlank(message = "Role cannot be blank")
    private String role;
}