package kz.example.vccollections.dto;

import kz.example.dto.UserResponseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CollectionItemEntryDto {
    private Long collectionId;
    private Long itemId;
    private Integer addedByUserId;
    private String addedAt;
    private Integer position;
    private String notes;
    private MediaItemDto mediaItem;
    private UserResponseDto addedByUser;
}
