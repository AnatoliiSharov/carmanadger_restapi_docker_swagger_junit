package ua.foxminded.asharov.cardatamanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.asharov.cardatamanager.dto.CategoryDto;

@Service
public interface CategoryService {

    @Transactional
    CategoryDto enterByManufacrureNameByModelNameByCategoryDto(String string, String string2, CategoryDto categoryDto);

    List<CategoryDto> retrieveAllByModelByManufacturer(String manufacturerName, String modelName);

    CategoryDto retrieveByModelByManufacturerByCategoryName(String manufacturerName, String modelName,
            String categoryName);

    void removeCategoryByModelNameByManufacturerNameByCategoryName(String manufacturerName, String modelName,
            String categoryName);

}
