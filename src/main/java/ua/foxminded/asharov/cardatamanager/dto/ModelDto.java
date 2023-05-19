package ua.foxminded.asharov.cardatamanager.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "entity of model of car from manufacturer")
public class ModelDto {
    @Schema(description = "entity key of model of car from manufacturer is giving automatically")
    private Long id;
    @NotEmpty(message = "name of model cannot be empty.")
    @Size(min = 2, max = 100, message = "The length of model cannot be less than 2 and more than 100 symbols.")
    @Schema(description = "name of model cannot be empty, any symbol within range, unique within single manufacturer", example = "7 Series")
    private String name;
    @Schema(description = "entity key of manufacturer is giving automatically a while creating, just exist or not exist in the system")
    private Long manufacturerId;
}
