package kz.example.vccollections.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {
    @NotBlank(message = "Comment text cannot be blank")
    @Size(max = 2000, message = "Comment text must not exceed 2000 characters")
    private String commentText;
}