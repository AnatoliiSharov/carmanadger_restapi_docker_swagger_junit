package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.dto.ModelDto;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.exception.ResourceAlreadyExistsException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelRepository;
import ua.foxminded.asharov.cardatamanager.service.ModelService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {
    public static final String MODEL_SERVICE_IMPL = "ModelServiceImpl";
    public static final String MANUFACTURER_NAME = "manufacturerName = ";
    public static final String MODEL_NAME = "modelName = ";

    private final ManufacturerRepository manufacturerRep;
    private final ModelRepository modelRep;

    @Override
    public List<ModelDto> retrieveByManufacturerName(String manufacturerName) {
        log.debug("ModelServiceImpl.retrieveByManufacturer started with manufacturerName = {}, modelName = {}",
                manufacturerName);
        List<Manufacturer> iterimManufacturers = manufacturerRep.findByName(manufacturerName);

        if (!iterimManufacturers.isEmpty()) {
            return toDtoList(modelRep.findByManufacturer(iterimManufacturers.get(0)));
        } else {
            throw new ResourceNotFoundException(MODEL_SERVICE_IMPL, MANUFACTURER_NAME + manufacturerName);
        }
    }

    @Override
    public ModelDto enter(String manufacturerName, ModelDto modelDto) {
        log.debug("ModelServiceImpl.enter started with manufacturerName = {}, modelDto = {}", manufacturerName,
                modelDto);
        Model result = new Model();

        if (modelDto.getId() != null) {
            result = modelRep.findById(modelDto.getId()).orElseThrow(
                    () -> new ResourceNotFoundException(MODEL_SERVICE_IMPL, "modelId =" + modelDto.getId()));
        }

        result.setManufacturer(manufacturerRep.findById(modelDto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException(MODEL_SERVICE_IMPL,
                        "manufacturerId =" + modelDto.getManufacturerId())));

        if (modelRep.existsByNameAndManufacturer(modelDto.getName(), result.getManufacturer())) {

            if (result.getId() != null) {

                if (!result.getName().equals(modelDto.getName())) {

                    throw new ResourceAlreadyExistsException(MODEL_SERVICE_IMPL, MODEL_NAME + modelDto.getName());
                }
            } else {
                throw new ResourceAlreadyExistsException(MODEL_SERVICE_IMPL, MODEL_NAME + modelDto.getName());
            }
        }
        result.setName(modelDto.getName());
        return toDto(modelRep.save(result));
    }

    @Override
    public void removeByManufacturerNameByModelName(String manufacturerName, String modelName) {
        log.debug(
                "ModelServiceImpl.removeByManufacturerNameByModelName started with manufacturerName = {}, modelName = {}",
                manufacturerName, modelName);
        modelRep.delete(retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName));
    }

    @Override
    public ModelDto retrieveByManufacturerNameByModelName(String manufacturerName, String modelName) {
        log.debug(
                "ModelServiceImpl.retrieveByManufacturerNameByModelName started with manufacturerName = {}, modelName = {}",
                manufacturerName, modelName);
        return toDto(retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName));
    }

    @Override
    public Model retrieveEntityByManufacturerNameByModelName(String manufacturerName, String modelName) {
        log.debug(
                "ModelServiceImpl.retrieveByManufacturerNameByModelNameWorkWithEntity started with manufacturerName = {}, modelName = {}",
                manufacturerName, modelName);
        List<Manufacturer> iterimManufacturers = manufacturerRep.findByName(manufacturerName);
        List<Model> result;

        if (!iterimManufacturers.isEmpty()) {
            result = modelRep.findByNameAndManufacturer(modelName, iterimManufacturers.get(0));
        } else {
            throw new ResourceNotFoundException(MODEL_SERVICE_IMPL, MANUFACTURER_NAME + manufacturerName);
        }

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            throw new ResourceNotFoundException(MODEL_SERVICE_IMPL, MODEL_NAME + modelName);
        }
    }

    private ModelDto toDto(Model entity) {
        return ModelDto.builder().id(entity.getId()).name(entity.getName())
                .manufacturerId(entity.getManufacturer().getId()).build();
    }

    public List<ModelDto> toDtoList(List<Model> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

}
