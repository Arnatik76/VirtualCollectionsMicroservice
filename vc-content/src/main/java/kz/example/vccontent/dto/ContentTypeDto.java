package kz.example.vccontent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentTypeDto {
    private Long typeId;
    private String typeName;
    private String typeIcon;
}
