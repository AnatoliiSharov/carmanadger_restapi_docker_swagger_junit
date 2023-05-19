package ua.foxminded.asharov.cardatamanager.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.dto.CarDto;
import ua.foxminded.asharov.cardatamanager.dto.SearchObject;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.exception.ResourceAlreadyExistsException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.CarRepository;
import ua.foxminded.asharov.cardatamanager.repository.CategoryRepository;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelRepository;
import ua.foxminded.asharov.cardatamanager.repository.YearRepository;
import ua.foxminded.asharov.cardatamanager.service.CarService;
import ua.foxminded.asharov.cardatamanager.specifications.CarSpecificationBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {
    public static final String CAR_SERVICE_IMPL = "CarServiceImpl";
    public static final String OBJECT_ID = "objectId = ";

    private final CarSpecificationBuilder carSpecificationBuilder;
    private final ManufacturerRepository manufacturerRep;
    private final CategoryRepository categoryRep;
    private final ModelRepository modelRep;
    private final YearRepository yearRep;
    private final CarRepository carRep;

    @Override
    public CarDto enter(CarDto carDto) {
        log.debug("started CarServiceImpl/enter with carDto = {}", carDto);
        Car result;

        if (carDto.getId() != null) {
            result = carRep.findById(carDto.getId()).orElseThrow(
                    () -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "manufacturerId = " + carDto.getId()));

            if (carDto.getObjectId() != null && result.getObjectId().equals(carDto.getObjectId())) {
                checkObjectIdAlreadyExist(carDto.getObjectId());
            }

            if (!carDto.getManufacturerId().equals(result.getManufacturer().getId())
                    && !carDto.getModelId().equals(result.getModel().getId())
                    && !carDto.getYearId().equals(result.getYear().getId())
                    && !carDto.getCategoryId().equals(result.getCategory().getId())) {
                checkExistentCarByManufacturerIdAndModelIdAndYearIdAndCategoryId(carDto);
            }
            return toDto(carRep.save(toEntity(carDto)));

        } else {
            checkObjectIdAlreadyExist(carDto.getObjectId());
            checkExistentCarByManufacturerIdAndModelIdAndYearIdAndCategoryId(carDto);
            return toDto(carRep.save(toEntity(carDto)));
        }
    }

    private void checkExistentCarByManufacturerIdAndModelIdAndYearIdAndCategoryId(CarDto carDto) {

        if (carRep.existsByManufacturerIdAndModelIdAndYearIdAndCategoryId(carDto.getManufacturerId(),
                carDto.getModelId(), carDto.getYearId(), carDto.getCategoryId())) {
            throw new ResourceAlreadyExistsException(CAR_SERVICE_IMPL, OBJECT_ID + carDto.getObjectId());
        }
    }

    private void checkObjectIdAlreadyExist(String objectId) {
        
        if (carRep.existsByObjectId(objectId)) {
            throw new ResourceAlreadyExistsException(CAR_SERVICE_IMPL, OBJECT_ID + objectId);
        }
        
    }

    private CarDto toDto(Car entity) {
        return CarDto.builder().objectId(entity.getObjectId()).manufacturerId(entity.getManufacturer().getId())
                .modelId(entity.getModel().getId()).categoryId(entity.getCategory().getId())
                .yearId(entity.getYear().getId()).build();
    }

    private Car toEntity(CarDto carDto) {
        Car result = new Car();

        result.setId(carDto.getId());
        result.setObjectId(carDto.getObjectId());
        result.setManufacturer(manufacturerRep.findById(carDto.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException(CAR_SERVICE_IMPL,
                        "ManufacturerId = " + carDto.getManufacturerId())));
        result.setModel(modelRep.findById(carDto.getModelId()).orElseThrow(
                () -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "modelId = " + carDto.getModelId())));
        result.setCategory(categoryRep.findById(carDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "categoryId = " + carDto.getCategoryId())));
        result.setYear(yearRep.findById(carDto.getYearId())
                .orElseThrow(() -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "yearId = " + carDto.getYearId())));
        return result;
    }

    @Override
    public Page<CarDto> retrieveAll(Pageable pageable, SearchObject searchObject) {
        log.debug("started CarServiceImpl/retrieveAll with pageable = {}, searchObject = {}", pageable, searchObject);
        
        if(searchObject != null) {
            return toDto(carRep.findAll(carSpecificationBuilder.toSpecification(searchObject), pageable));
        }else {
            return toDto(carRep.findAll(pageable));
        }
    }

    private Page<CarDto> toDto(Page<Car> entities) {
        return entities.map(this::toDto);
    }

}
