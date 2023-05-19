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
@Schema(description = "entity of category of car(vehicle body type)")
public class CategoryDto {
    @Schema(description = "entity key of of category of car is creating automaticly")
    private Long id;
    @NotEmpty(message = "name of category cannot be empty.")
    @Size(min = 2, max = 100, message = "The length of category cannot be less than 2 and more than 100 symbols.")
    @Schema(description = "cannot be empty, m.b. beloning several models of any manufacturer, any symbols within range", example = "Pickup")
    private String name;
    
    public CategoryDto(String name) {
        super();
        this.name = name;
    }
   
}
