package kz.example.vccollections.dto;

import kz.example.dto.UserResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionItemResponseDto {
    private Long collectionId;
    private Long itemId;
    private UserResponseDto addedByUser;
    private OffsetDateTime addedAt;
    private Integer position;
    private String notes;
    private MediaItemDto mediaItem;
}