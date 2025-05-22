package kz.example.vccollections.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCollectionItemRequestDto {
    private String notes;
    private Integer position;
}