package ua.foxminded.asharov.cardatamanager.service.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.asharov.cardatamanager.dto.CarDto;
import ua.foxminded.asharov.cardatamanager.dto.CategoryDto;
import ua.foxminded.asharov.cardatamanager.dto.ManufacturerDto;
import ua.foxminded.asharov.cardatamanager.dto.ModelDto;
import ua.foxminded.asharov.cardatamanager.dto.YearDto;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.entity.Category;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.Year;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.CategoryRepository;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelRepository;
import ua.foxminded.asharov.cardatamanager.repository.YearRepository;

@Service
@Transactional
public class MapperUtil {
    private static final String CAR_SERVICE_IMPL = "CarServiceImpl";
    
    private final ManufacturerRepository manufacturerRep;
    private final CategoryRepository categoryRep;
    private final ModelRepository modelRep;
    private final YearRepository yearRep;
    private final ModelMapper modelMapper;
    
    public MapperUtil(ManufacturerRepository manufacturerRep, CategoryRepository categoryRep, ModelRepository modelRep,
            YearRepository yearRep) {
        super();
        this.manufacturerRep = manufacturerRep;
        this.categoryRep = categoryRep;
        this.modelRep = modelRep;
        this.yearRep = yearRep;
        this.modelMapper = new ModelMapper();
    }

    public CarDto toDto(Car entity) {
        return modelMapper.map(entity, CarDto.class);
    }

    public Car toEntity(CarDto dto) {
        Car result = modelMapper.map(dto, Car.class);

        result.setId(dto.getId());
        result.setManufacturer(manufacturerRep.findById(dto.getManufacturerId()).orElseThrow(
                () -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "manufacturerId=" + dto.getManufacturerId())));
        result.setCategory(categoryRep.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "categoryId=" + dto.getCategoryId())));
        result.setYear(yearRep.findById(dto.getYearId())
                .orElseThrow(() -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "yearId=" + dto.getYearId())));
        result.setModel(modelRep.findById(dto.getModelId())
                .orElseThrow(() -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "modelId=" + dto.getModelId())));
        return result;
    }

    public ModelDto toDto(Model entity) {
        return modelMapper.map(entity, ModelDto.class);
    }

    public Model toEntity(ModelDto dto) {
        Model result = modelMapper.map(dto, Model.class);

        result.setManufacturer(manufacturerRep.findById(dto.getManufacturerId()).orElseThrow(
                () -> new ResourceNotFoundException(CAR_SERVICE_IMPL, "manufacturerId=" + dto.getManufacturerId())));
        return result;
    }

    public CategoryDto toDto(Category entity) {
        return modelMapper.map(entity, CategoryDto.class);
    }

    public Category toEntity(CategoryDto dto) {
        return modelMapper.map(dto, Category.class);
    }

    public ManufacturerDto toDto(Manufacturer entity) {
        return new ManufacturerDto(entity.getId(), entity.getName());
    }

    public Manufacturer toEntity(ManufacturerDto dto) {
        return new Manufacturer(dto.getId(), dto.getName());
    }

    public YearDto toDto(Year entity) {
        return modelMapper.map(entity, YearDto.class);
    }

    public Year toEntity(YearDto dto) {
        return modelMapper.map(dto, Year.class);
    }

}
