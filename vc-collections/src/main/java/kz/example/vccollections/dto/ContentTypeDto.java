package kz.example.vccollections.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentTypeDto {
    private Long typeId;
    private String typeName;
    private String typeIcon;
}