package kz.example.vccollections.dto;

import kz.example.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionCollaboratorResponseDto {
     private Long collectionId;
    // private Long userId;
    private String role;
    private OffsetDateTime joinedAt;
    private UserResponseDto user;
}