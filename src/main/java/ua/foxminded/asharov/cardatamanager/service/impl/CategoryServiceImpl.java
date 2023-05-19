package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.dto.CategoryDto;
import ua.foxminded.asharov.cardatamanager.entity.Category;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLink;
import ua.foxminded.asharov.cardatamanager.exception.ResourceIsStillUsedException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.CarRepository;
import ua.foxminded.asharov.cardatamanager.repository.CategoryRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelAndCategoryLinkRepository;
import ua.foxminded.asharov.cardatamanager.service.CategoryService;
import ua.foxminded.asharov.cardatamanager.service.ModelService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_SERVICE_IMPL = "CategoryServiceImpl";

    private final ModelAndCategoryLinkRepository modelAndCategoryRep;
    private final CategoryRepository categoryRep;
    private final CarRepository carRep;
    private final ModelService modelServ;

    @Override
    public List<CategoryDto> retrieveAllByModelByManufacturer(String manufacturerName, String modelName) {
        log.debug("YearCategoryImpl.retrieveAllByModelByManufacturer started with manufacturerName = {}, modelName = {}",
                manufacturerName, modelName);
        return toDtoList(modelAndCategoryRep
                .findByModel(modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName))
                .stream().map(ModelAndCategoryLink::getCategory).collect(Collectors.toList()));
    }

    @Override
    public CategoryDto retrieveByModelByManufacturerByCategoryName(String manufacturerName, String modelName, String categoryName) {
        log.debug(
                "YearCategoryImpl.retrieveByModelByManufacturerByYearName started with manufacturerName = {}, modelName = {}, yearName = {}",
                manufacturerName, modelName, categoryName);
        Model model = modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName);
        List<Category> interimCategory = categoryRep.findByName(categoryName);
        if (interimCategory.isEmpty()) {
            throw new ResourceNotFoundException(CATEGORY_SERVICE_IMPL, "categoryName = " + categoryName);
        }
        return toDto(modelAndCategoryRep.findById(new ModelAndCategoryLink(model, interimCategory.get(0)).getId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_SERVICE_IMPL,"ModelAndYearLink = " + new ModelAndCategoryLink(model, interimCategory.get(0))))
                .getCategory());
    }

    @Override
    public CategoryDto enterByManufacrureNameByModelNameByCategoryDto(String manufacturerName, String modelName,
            CategoryDto categoryDto) {
        log.debug(
                "YearCategoryImpl.enterByManufacrureNameByModelNameByYearDto started with manufacturerName = {}, modelName = {}, yearDto = {}",
                manufacturerName, modelName, categoryDto);
        Model model = modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName);
        List<Category> interimCategory = categoryRep.findByName(categoryDto.getName());
        Category category;

        if (!interimCategory.isEmpty()) {
            category = interimCategory.get(0);
        } else {
            category = categoryRep.save(toEntity(categoryDto));
        }
        ModelAndCategoryLink result = modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId())
                .orElse(modelAndCategoryRep.save(new ModelAndCategoryLink(model, category)));
        return toDto(result.getCategory());
    }

    @Override
    public void removeCategoryByModelNameByManufacturerNameByCategoryName(String manufacturerName, String modelName,
            String categoryName) {
        log.debug(
                "YearCategoryImpl.removeYearByModelNameByManufacturerNameByYearName started with manufacturerName = {}, modelName = {}, yearName = {}",
                manufacturerName, modelName, categoryName);
        Model model = modelServ.retrieveEntityByManufacturerNameByModelName(manufacturerName, modelName);
        List<Category> interimCategory = categoryRep.findByName(categoryName);

        if (interimCategory.isEmpty()) {
            throw new ResourceNotFoundException(CATEGORY_SERVICE_IMPL, "category =" + categoryName);
        }
        ModelAndCategoryLink modelAndCategoryLink = new ModelAndCategoryLink(model, interimCategory.get(0));

        if (modelAndCategoryRep.existsById(modelAndCategoryLink.getId())) {

            if (carRep.findAllByCategory(interimCategory.get(0)).isEmpty()) {
                modelAndCategoryRep.delete(modelAndCategoryLink);

                if (modelAndCategoryRep.findAllByCategory(interimCategory.get(0)).isEmpty()) {
                    categoryRep.delete(interimCategory.get(0));
                }
            } else {
                throw new ResourceIsStillUsedException(CATEGORY_SERVICE_IMPL,
                        "cars =" + carRep.findAllByCategory(interimCategory.get(0)));
            }
        } else {
            throw new ResourceNotFoundException(CATEGORY_SERVICE_IMPL, "ModelAndYearLink=" + modelAndCategoryLink);
        }
    }


    public CategoryDto toDto(Category entity) {
        return CategoryDto.builder().id(entity.getId()).name(entity.getName()).build();
    }

    public Category toEntity(CategoryDto dto) {
        return Category.builder().id(dto.getId()).name(dto.getName()).build();
    }

    public List<CategoryDto> toDtoList(List<Category> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<CategoryDto> toDtoList(Iterable<Category> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).map(this::toDto).collect(Collectors.toList());
    }

}
