package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequestCreateDto {
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank
    @Size(max = 500)
    private String description;
}