package ua.foxminded.asharov.cardatamanager.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "entity of year of manufacture")
public class YearDto {
    
    @Schema(description = "key of year of manufacture is giving automatically")
    private Long id;
    @Min(value = 1889, message = "Manufacture years cannot be less than 1889 and more than 2050.")
    @Max(value = 2050, message = "Manufacture years cannot be less than 1889 and more than 2050.")
    @Schema(description = "year in the form YYYY, unique in system, m.b. belonging several models of different manufacturers", example = "1998")
    private Integer year;
    
    public YearDto(Integer year) {
        super();
        this.year = year;
    }

}
