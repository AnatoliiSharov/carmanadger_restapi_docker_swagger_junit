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
import ua.foxminded.asharov.cardatamanager.dto.ModelDto;
import ua.foxminded.asharov.cardatamanager.service.ModelService;

@RestController
@RequestMapping("/api/v1/manufacturers/{manufacturer}/models")
@RequiredArgsConstructor
@Tag(name="models", description="view, create, update, delete model")
public class ManufacturersModelController {
    static final String MANUFACTURER = "manufacturer"; 
    static final String MODEL = "model"; 
    static final String MODELS = "models"; 
    static final String START_PASS = "/api/v1/manufacturers"; 

    private final ModelService modelServ;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get models", description = "return list of all models of manufacturer")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Get all models", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ModelDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer not found", 
        content = @Content) })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ModelDto> showManufacturerAllModels(@PathVariable(MANUFACTURER)@Parameter(description = "manufacturer name") String manufacturerName) {
        return modelServ.retrieveByManufacturerName(manufacturerName);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get model", description = "return model of manufacturer by model name")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Get model", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ModelDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Manufacturer or Model not found", 
      content = @Content) })
    @GetMapping("/{model}")
    @ResponseStatus(HttpStatus.OK)
    public ModelDto showManufacturerModel(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name")String manufacturerName, 
            @PathVariable(MODEL) @Parameter(description = "model name")String modelName) {
        return modelServ.retrieveByManufacturerNameByModelName(manufacturerName, modelName);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create model", description = "create model based on manufacturer and return this modelDto")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Create model", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ModelDto.class)) }),
      @ApiResponse(responseCode = "400", description = "model already exists", 
      content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = ModelDto.class)) }),
      @ApiResponse(responseCode = "404", description = "manufacturer not found", 
        content = @Content) })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ModelDto createNewModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name")String manufacturerName, 
            @RequestBody @Valid @Parameter(description = "ModelDto object") ModelDto modelDto) {
        return modelServ.enter(manufacturerName, modelDto);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update model", description = "Update model and return this modelDto")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Model updated", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ModelDto.class)) }),
      @ApiResponse(responseCode = "400", description = "Model already exists",
        content = @Content), 
      @ApiResponse(responseCode = "404", description = "Manufacturer not found", 
        content = @Content) })
    @PostMapping("/{model}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> updateModelWithManufacturer(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name") String manufacturerName, 
            @RequestBody @Valid @Parameter(description = "ModelDto object") ModelDto modelDto) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{model}")
                .buildAndExpand(modelServ.enter(manufacturerName, modelDto).getName())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete model")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Model deleted", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = ModelDto.class)) }),
      @ApiResponse(responseCode = "404", description = "Model not found", 
        content = @Content) })
    @DeleteMapping("/{model}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteManufacturerModel(@PathVariable(MANUFACTURER) @Parameter(description = "manufacturer name") String manufacturerName, 
            @PathVariable(MODEL)@Parameter(description = "model name", example = "6 Series") String modelName) {
        modelServ.removeByManufacturerNameByModelName(manufacturerName, modelName);
    }

}
