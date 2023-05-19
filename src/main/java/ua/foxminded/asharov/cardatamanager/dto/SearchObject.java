package ua.foxminded.asharov.cardatamanager.dto;

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
@Schema(description = "set of parameters for user filtring")
public class SearchObject {
    @Schema(description = "array of name of model for displaying records with this value as model name ", example = "{XC70, Tundra Double Cab, 6 Series}")   
    String[] model;
    @Schema(description = "array of name of category for displaying records with this value as model name", example = "{Sedan, Pickup, SUV}")
    String[] category;
    @Schema(description = "array of name of manufacturer for displaying records with this value as model name", example = "{Toyota, Volvo, BMW}")
    String[] manufacturer;
    @Schema(description = "year in YYYY format for displaying years that are smaller than this", example = "2019")
    String maxYear;
    @Schema(description = "year in YYYY format for displaying years that are longer than this", example = "2019")
    String minYear;

}
