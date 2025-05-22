package kz.example.vccollections.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCollectionRequestDto {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @Size(max = 255, message = "Cover image URL must not exceed 255 characters")
    private String coverImageUrl;

    @NotNull(message = "isPublic flag must be provided")
    private Boolean isPublic;
}