package kz.example.vccollections.dto;

import kz.example.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionCommentResponseDto {
    private Integer commentId;
    private Long collectionId;
    // private Integer userId;
    private String commentText;
    private OffsetDateTime createdAt;
    private UserResponseDto user;
}