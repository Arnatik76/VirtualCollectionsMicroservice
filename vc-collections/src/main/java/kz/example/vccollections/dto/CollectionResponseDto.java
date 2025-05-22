package kz.example.vccollections.dto;

import kz.example.dto.UserResponseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponseDto {
    @JsonProperty("collectionId")
    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
//    private Long userId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean isPublic;
    private Integer viewCount;
    private UserResponseDto owner;
    private List<CollectionItemEntryDto> items;
    private List<CollectionCollaboratorResponseDto> collaborators;
    private Integer likeCount;
    private Integer commentCount;
}