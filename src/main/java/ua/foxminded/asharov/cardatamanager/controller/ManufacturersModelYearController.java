package ua.foxminded.asharov.cardatamanager.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ua.foxminded.asharov.cardatamanager.dto.YearDto;
import ua.foxminded.asharov.cardatamanager.service.YearService;

@RestController
@RequestMapping("/api/v1/manufacturers/{manufacturer}/models/{model}")
@RequiredArgsConstructor
@Tag(name="years", description="view, create, update, delete year")
public class ManufacturersModelYearController {
    static final String MANUFACTURER = "manufacturer"; 
    static final String MODEL = "model"; 
    static final String YEAR = "year"; 

    private final YearService yearServ;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get years", description = "return list of all years of models of manufacturer")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Get all models", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = YearDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer or Model not found", 
      content = @Content) })
    @GetMapping("/years")
    @ResponseStatus(HttpStatus.OK)
    public List<YearDto> findAllYearsWithModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name")String manufacturerName, 
            @PathVariable(MODEL)@Parameter(description = "model name") String modelName) {
        return yearServ.retrieveAllByModelByManufacturer(manufacturerName, modelName);
                
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get year", description = "return years of models of manufacturer")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Get year", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = YearDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer or Model or Year not found", 
      content = @Content) })
    @GetMapping("/{year}")
    @ResponseStatus(HttpStatus.OK)
    public YearDto findYearWithModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name")String manufacturerName, 
            @PathVariable(MODEL)@Parameter(description = "model name") String modelName, 
            @PathVariable(YEAR) @Parameter(description = "year name")String year) {
        return yearServ.retrieveByModelByManufacturerByYearName(manufacturerName, modelName, year);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create year", description = "Create and return year of models of manufacturer")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Create year with model", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = YearDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Model or Manufacturer not found", 
      content = @Content) })
    @PutMapping("/year")
    @ResponseStatus(HttpStatus.OK)
    public YearDto createYearsWithModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name") String manufacturerName, 
            @PathVariable(MODEL) @Parameter(description = "model name") String modelName, 
            @RequestBody @Valid @Parameter(description = "yearDto with key null")YearDto yearDto) {
        return yearServ.enterByManufacrureNameByModelNameByYearDto(manufacturerName, modelName, yearDto);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update year", description = "Update and return year of models of manufacturer")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Update year with model", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = YearDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Model or Manufacturer not found", 
      content = @Content) })
    @PostMapping("/year")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> updateYearsWithModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name")String manufacturerName, 
            @PathVariable(MODEL) @Parameter(description = "model name")String modelName, 
            @RequestBody @Valid @Parameter(description = "existen yearDto") YearDto yearDto) {
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{year}")
                .buildAndExpand(yearServ.enterByManufacrureNameByModelNameByYearDto(manufacturerName, modelName, yearDto).getYear())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete year", description = "Delete year and return void")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Deleted year", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = YearDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer or Model or Year not found/", 
      content = @Content),
      @ApiResponse(responseCode = "400", description = "Year still used", 
      content = @Content) })
    @DeleteMapping("/{year}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteYearWithModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name") String manufacturerName, 
            @PathVariable(MODEL) @Parameter(description = "model name") String modelName, 
            @PathVariable(YEAR) @Parameter(description = "model name") String year) {
        yearServ.removeYearByModelNameByManufacturerNameByYearName(manufacturerName, modelName, year);
    }
    
}
