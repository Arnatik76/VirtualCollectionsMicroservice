package kz.example.vccontent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateMediaItemRequestDto {

    @NotBlank(message = "Title cannot be blank.")
    @Size(max = 255, message = "Title must not exceed 255 characters.")
    private String title;

    @NotNull(message = "Type ID cannot be null.")
    private Long typeId;

    @Size(max = 255, message = "Creator must not exceed 255 characters.")
    private String creator;

    private String description;

    @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters.")
    private String thumbnailUrl;

    @Size(max = 255, message = "External URL must not exceed 255 characters.")
    private String externalUrl;

    private LocalDate releaseDate;

    private List<String> tags;
}