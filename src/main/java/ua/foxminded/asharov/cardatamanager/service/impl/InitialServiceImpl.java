package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.entity.Category;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLink;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLink;
import ua.foxminded.asharov.cardatamanager.entity.Year;
import ua.foxminded.asharov.cardatamanager.repository.CarRepository;
import ua.foxminded.asharov.cardatamanager.repository.CategoryRepository;
import ua.foxminded.asharov.cardatamanager.repository.CleanRepositoryImpl;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelAndCategoryLinkRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelAndYearLinkRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelRepository;
import ua.foxminded.asharov.cardatamanager.repository.YearRepository;
import ua.foxminded.asharov.cardatamanager.service.InitialService;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class InitialServiceImpl implements InitialService {

    private final CleanRepositoryImpl cleanRep;
    private final ModelAndCategoryLinkRepository modelAndCategoryRep;
    private final ModelAndYearLinkRepository modelAndYearRep;
    private final ManufacturerRepository manufacturerRep;
    private final CategoryRepository categoryRep;
    private final ModelRepository modelRep;
    private final YearRepository yearRep;
    private final CarRepository carRep;

    @Override
    public void cleanDataBase() {
        log.debug("started InitialServiceImpl.cleanDataBase");
        cleanRep.deleteCar();
        cleanRep.deleteModelAndCategory();
        cleanRep.deleteModelAndYear();
        cleanRep.deleteModel();
        cleanRep.deleteYear();
        cleanRep.deleteCategory();
        cleanRep.deleteManufacturer();
    }

    @Override
    public void loadDataBaseFromFile(String makeName, String modelName, String yearName, String categoryNames,
            String objectIdName) {
        Manufacturer manufacturer = getManufacturerByName(makeName);

        Model model = getByModelNameAndManufacturer(modelName, manufacturer);

        Year year = getByYearValueAndModel(Integer.parseInt(yearName), model);

        List<Category> listCategory = Stream.of(categoryNames.replace("\"", "").split(",")).map(String::trim)
                .map(each -> getByCategoryNameAndModel(each, model)).collect(Collectors.toList());

        for (Category category : listCategory) {
            if (!carRep.existsByObjectId(objectIdName)) {
                carRep.save(Car.builder().objectId(objectIdName).manufacturer(manufacturer).model(model)
                        .category(category).year(year).build());
            }
        }
    }

    private Category getByCategoryNameAndModel(String categoryName, Model model) {
        Category category;

        if (categoryRep.existsByName(categoryName)) {
            category = categoryRep.findByName(categoryName).get(0);
        } else {
            category = categoryRep.save(new Category(categoryName));
        }
        return modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId())
                .orElse(modelAndCategoryRep.save(new ModelAndCategoryLink(model, category))).getCategory();
    }

    private Year getByYearValueAndModel(int yearValue, Model model) {
        Year year;

        if (yearRep.existsByYear(yearValue)) {
            year = yearRep.findByYear(yearValue).get(0);
        } else {
            year = yearRep.save(new Year(yearValue));
        }
        return modelAndYearRep.findById(new ModelAndYearLink(model, year).getId())
                .orElse(modelAndYearRep.save(new ModelAndYearLink(model, year))).getYear();
    }

    private Model getByModelNameAndManufacturer(String modelName, Manufacturer manufacturer) {

        if (modelRep.existsByNameAndManufacturer(modelName, manufacturer)) {
            return modelRep.findByNameAndManufacturer(modelName, manufacturer).get(0);
        } else {
            return modelRep.save(Model.builder().name(modelName).manufacturer(manufacturer).build());
        }
    }

    private Manufacturer getManufacturerByName(String makeName) {

        if (manufacturerRep.existsByName(makeName)) {
            return manufacturerRep.findByName(makeName).get(0);
        } else {
            return manufacturerRep.save(new Manufacturer(makeName));
        }
    }
}
