package ua.foxminded.asharov.cardatamanager.dto;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(accessMode = Schema.AccessMode.READ_ONLY)
public class ModelAndYearLinkDto {
    private Long modelId;
    private Long yearId;
}
