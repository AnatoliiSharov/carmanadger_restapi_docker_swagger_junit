package ua.foxminded.asharov.cardatamanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private int statusCode;

    private String path;
    private String error;

    private String message;
}

