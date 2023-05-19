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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;
import ua.foxminded.asharov.cardatamanager.dto.ManufacturerDto;
import ua.foxminded.asharov.cardatamanager.service.ManufacturerService;

@RestController
@RequestMapping("/api/v1/manufacturers")
@RequiredArgsConstructor
@Tag(name="manufacturers", description="view, create, update, delete manufacturer")
public class ManufacturerController {
    static final String MANUFACTURER = "manufacturer";
    
    private final ManufacturerService manufacturerServ;
    
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get manufacturers", description = "Found list of all manufacturers and give their back as list dtos")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found list of all manufacturers and give their back as list dtos", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ManufacturerDto.class)) }),
      })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ManufacturerDto> findAllManufacturers() {
        return manufacturerServ.retrieveAll();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get manufacturer", description = "Found manufacturer by name and give it back as manufacturerDto")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found manufacturer by name and give it back as manufacturerDto", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = String.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer not found", 
        content = @Content) })
    @GetMapping("/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public ManufacturerDto findManufacturerByName(@PathVariable(MANUFACTURER)@Parameter(description = "name of manufacturer") String nameMaker) {
        return manufacturerServ.retrieveByName(nameMaker);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create manufacturer", description = "Create manufacturer and give it as new manufacturerDto")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Create manufacturer and give it  back as new manufacturerDto", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ManufacturerDto.class)) }),
      @ApiResponse(responseCode = "400", description = "Manufacturer already exists", 
        content = @Content)})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createNewManufacturer(@RequestBody @Valid @Parameter(description = "ManufacturerDto with id = null")ManufacturerDto manufacturerDto) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{manufacturer}")
                .buildAndExpand(manufacturerServ.enter(manufacturerDto).getName())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update manufacturer", description = "Update manufacturer and give it back as new manufacturerDto")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Update manufacturer and give it  back as new manufacturerDto", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ManufacturerDto.class)) }),
      @ApiResponse(responseCode = "400", description = "Manufacturer already exists", 
        content = @Content), 
      @ApiResponse(responseCode = "404", description = "Manufacturer not found", 
        content = @Content) })
    @PutMapping("/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public ManufacturerDto updateManufacturer(@RequestBody @Valid @Parameter(description = "ManufacturerDto with existent id ")ManufacturerDto manufacturerDto) {
        return manufacturerServ.enter(manufacturerDto);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete manufacturer", description = "Delete manufacturer, give back void")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Delete manufacturer", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ManufacturerDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer not found", 
        content = @Content) })
    @DeleteMapping("/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name") String makerName) {
        manufacturerServ.removeByName(makerName);
    }

}
