package kz.example.vccollections.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCollectionRequestDto {
    private String title;
    private String description;
    private String coverImageUrl;
    private Boolean isPublic = true;
}
