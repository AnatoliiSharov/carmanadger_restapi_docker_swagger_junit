package ua.foxminded.asharov.cardatamanager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import ua.foxminded.asharov.cardatamanager.dto.ErrorDto;
import ua.foxminded.asharov.cardatamanager.exception.ApiException;

@ControllerAdvice
@Hidden
@Tag(name="Hendler of exceptions", description="list of specify api exceptions")
public class ControllerExceptionHandler {

    ResponseEntity<ErrorDto> handApiExceptions(ApiException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getHttpStatus().value())
                .body(ErrorDto.builder().statusCode(ex.getHttpStatus().value()).path(request.getRequestURI())
                        .error(ex.getHttpStatus().getReasonPhrase()).message(ex.getMessage()).build());
    }

}
