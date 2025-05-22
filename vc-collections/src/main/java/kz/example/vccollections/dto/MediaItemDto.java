package kz.example.vccollections.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaItemDto {
    private Long itemId;
    private Long typeId;
    private String title;
    private String creator;
    private String description;
    private String thumbnailUrl;
    private String externalUrl;
    private Date releaseDate;
    private OffsetDateTime addedAt;
    private ContentTypeDto contentType;
    private List<TagDto> tags;
}