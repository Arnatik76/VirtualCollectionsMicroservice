package kz.example.vccollections.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    private Long tagId;
    private String tagName;
}