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
@Schema(description = "entity of year of manufacture")
public class ManufacturerDto {
    @Schema(description = "entity key of manufacturer is giving automatically")
    private Long id;
    @NotEmpty(message = "name of manufacturer cannot be empty")
    @Size(min = 2, max = 100, message = "The length of name cannot be less than 2 and more than 100 symbols.")
    @Schema(description = "cannot be empty, unique in system, any symbols within range", example = "Volvo")
    private String name;

    public ManufacturerDto(String name) {
        super();
        this.name = name;
    }

}
