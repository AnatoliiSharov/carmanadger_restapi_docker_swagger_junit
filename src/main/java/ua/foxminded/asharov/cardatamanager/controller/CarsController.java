package ua.foxminded.asharov.cardatamanager.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ua.foxminded.asharov.cardatamanager.dto.CarDto;
import ua.foxminded.asharov.cardatamanager.dto.SearchObject;
import ua.foxminded.asharov.cardatamanager.service.CarService;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@Tag(name="cars", description="view cars with capability of selection by parameters of car")
public class CarsController {

    private final CarService carServ;

    @Operation(summary = "Get cars", description = "Get cars with filtring and sortingand")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Get cars ", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = CarDto.class)) }) })
    @GetMapping(path = "/custom")
    public Page<CarDto> showCars(Pageable pageable, SearchObject searchObject) {
        return carServ.retrieveAll(pageable, searchObject);
    }

}
