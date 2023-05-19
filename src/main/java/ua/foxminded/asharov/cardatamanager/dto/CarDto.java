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
@Schema(description = "entity of car")
public class CarDto {
    @Schema(description = "entity key of car  is giving automatically")
    private Long id;
    @NotEmpty(message = "objectId of car cannot be empty")
    @Size(min = 10, max = 10, message = "The objectId of name cannot be different than 10 symbols.")
    @Schema(description = "cannot be empty is giving by admin, must be [a-zA-Z1-9] within range", example = "Kb9b2493oy")
    private String objectId;
    @Schema(description = "entity key of manufacturer is giving automatically a while creating, just exist or not exist in the system")
    private Long manufacturerId;
    @Schema(description = "entity key of year of manufacturer is giving automatically a while creating, just exist or not exist in the system")
    private Long yearId;
    @Schema(description = "entity key of model from manufacturer is giving automatically a while creating, just exist or not exist in the system")
    private Long modelId;
    @Schema(description = "entity key of category is giving automatically a while creating, just exist or not exist in the system")
    private Long categoryId;

}
