package kz.example.vccollections.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCollectionItemRequestDto {

    @NotNull(message = "Media item ID cannot be null")
    private Long mediaItemId;

    private String notes;
    private Integer position;
}