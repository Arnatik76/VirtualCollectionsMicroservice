package kz.example.vccontent.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateMediaItemRequestDto {

    @Size(max = 255, message = "Title must not exceed 255 characters.")
    private String title;

    // private Long typeId;

    @Size(max = 255, message = "Creator must not exceed 255 characters.")
    private String creator;

    private String description;

    @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters.")
    private String thumbnailUrl;

    @Size(max = 255, message = "External URL must not exceed 255 characters.")
    private String externalUrl;

    private LocalDate releaseDate; // yyyy-MM-dd

    private List<String> tags; // List of tag names to set (replaces existing tags)
}