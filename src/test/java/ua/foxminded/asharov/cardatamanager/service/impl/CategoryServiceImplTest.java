package ua.foxminded.asharov.cardatamanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.foxminded.asharov.cardatamanager.dto.CategoryDto;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.entity.Category;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLink;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLinkKey;
import ua.foxminded.asharov.cardatamanager.exception.ResourceIsStillUsedException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.CarRepository;
import ua.foxminded.asharov.cardatamanager.repository.CategoryRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelAndCategoryLinkRepository;
import ua.foxminded.asharov.cardatamanager.service.ModelService;

@SpringBootTest(classes = { CategoryServiceImpl.class })
class CategoryServiceImplTest {
    @MockBean
    ModelAndCategoryLinkRepository modelAndCategoryRep;
    @MockBean
    CategoryRepository categoryRep;
    @MockBean
    CarRepository carRep;
    @MockBean
    ModelService modelServ;
    @Autowired
    CategoryServiceImpl categoryServ;

    @Test
    void testRetrieveAllByModelByManufacturer() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        String expected = "[CategoryDto(id=3, name=SUV), CategoryDto(id=4, name=Wagon)]";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("XC70", "Volvo")).thenReturn(model);
        when(modelAndCategoryRep.findByModel(model))
                .thenReturn(Arrays.asList(new ModelAndCategoryLink(model, new Category(3L, "SUV")),
                        new ModelAndCategoryLink(model, new Category(4L, "Wagon"))));

        assertEquals(expected, categoryServ.retrieveAllByModelByManufacturer("XC70", "Volvo").toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(modelAndCategoryRep, times(1)).findByModel(any(Model.class));
    }

    @Test
    void testRetrieveByModelByManufacturerByCategoryName_shouldOk() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId()))
                .thenReturn(Optional.of(new ModelAndCategoryLink(model, category)));

        assertEquals(expected,
                categoryServ.retrieveByModelByManufacturerByCategoryName("Volvo", "XC70", "SUV").toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(1)).findById(any(ModelAndCategoryLinkKey.class));
    }

    @Test
    void testRetrieveByModelByManufacturerByCategoryName_shouldNotFoundException_whenNotExistWithCategoryName() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "[CategoryDto(id=3, year=SUV), CategoryDto(id=4, year=Wagon)]";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Collections.emptyList());
        when(modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId()))
                .thenReturn(Optional.of(new ModelAndCategoryLink(model, category)));

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryServ.retrieveByModelByManufacturerByCategoryName("Volvo", "XC70", "SUV");
        });

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(0)).findById(any(ModelAndCategoryLinkKey.class));
    }

    @Test
    void testRetrieveByModelByManufacturerByCategoryName_shouldNotFoundException_whenNotExistModelAndCategoryLink() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "[CategoryDto(id=3, year=SUV), CategoryDto(id=4, year=Wagon)]";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryServ.retrieveByModelByManufacturerByCategoryName("Volvo", "XC70", "SUV");
        });

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(1)).findById(any(ModelAndCategoryLinkKey.class));
    }

    @Test
    void testEnterByManufacrureNameByModelNameByCategoryDto_shouldOk_whenCategoryAlreadyExistButLinkStillNot() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(categoryRep.save(new Category(3L, "SUV"))).thenReturn(new Category(3L, "SUV"));
        when(modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId()))
                .thenReturn(Optional.of(new ModelAndCategoryLink(model, category)));
        when(modelAndCategoryRep.save(new ModelAndCategoryLink(model, category)))
                .thenReturn(new ModelAndCategoryLink(model, category));

        assertEquals(expected, categoryServ
                .enterByManufacrureNameByModelNameByCategoryDto("Volvo", "XC70", new CategoryDto("SUV")).toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(categoryRep, times(0)).save(any(Category.class));
        verify(modelAndCategoryRep, times(1)).findById(any(ModelAndCategoryLinkKey.class));
        verify(modelAndCategoryRep, times(1)).save(any(ModelAndCategoryLink.class));
    }

    @Test
    void testEnterByManufacrureNameByModelNameByCategoryDto_shouldOk_whenCategoryNotExistBefore() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Collections.emptyList());
        when(categoryRep.save(new Category(null, "SUV"))).thenReturn(new Category(3L, "SUV"));
        when(modelAndCategoryRep.findById(new ModelAndCategoryLink(model, category).getId()))
                .thenReturn(Optional.of(new ModelAndCategoryLink(model, category)));
        when(modelAndCategoryRep.save(new ModelAndCategoryLink(model, category)))
                .thenReturn(new ModelAndCategoryLink(model, category));

        assertEquals(expected, categoryServ
                .enterByManufacrureNameByModelNameByCategoryDto("Volvo", "XC70", new CategoryDto("SUV")).toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(categoryRep, times(1)).save(any(Category.class));
        verify(modelAndCategoryRep, times(1)).findById(any(ModelAndCategoryLinkKey.class));
        verify(modelAndCategoryRep, times(1)).save(any(ModelAndCategoryLink.class));
    }

    @Test
    void testRemoveCategoryByModelNameByManufacturerNameByCategoryName_shouldOk_whenLinkCategoryAndCategoryDeleted() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(modelAndCategoryRep.existsById(new ModelAndCategoryLink(model, category).getId())).thenReturn(true);
        when(modelAndCategoryRep.findAllByCategory(category)).thenReturn(Collections.emptyList());
        when(carRep.findAllByCategory(category)).thenReturn(Collections.emptyList());
        
        categoryServ.removeCategoryByModelNameByManufacturerNameByCategoryName("Volvo", "XC70", "SUV");

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(1)).existsById(any(ModelAndCategoryLinkKey.class));
        verify(carRep, times(1)).findAllByCategory(any(Category.class));
        verify(modelAndCategoryRep, times(1)).delete(any(ModelAndCategoryLink.class));
        verify(categoryRep, times(1)).delete(any(Category.class));
     }

    @Test
    void testRemoveCategoryByModelNameByManufacturerNameByCategoryName_shouldOk_whenLinkCategoryButCategoryStillUsing() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";
        
        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(modelAndCategoryRep.existsById(new ModelAndCategoryLink(model, category).getId())).thenReturn(true);
        when(carRep.findAllByCategory(category)).thenReturn(Collections.emptyList());
        when(modelAndCategoryRep.findAllByCategory(category)).thenReturn(Arrays.asList(new ModelAndCategoryLink(model, category)));
        
        
        categoryServ.removeCategoryByModelNameByManufacturerNameByCategoryName("Volvo", "XC70", "SUV");
        
        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(1)).existsById(any(ModelAndCategoryLinkKey.class));
        verify(carRep, times(1)).findAllByCategory(any(Category.class));
        verify(modelAndCategoryRep, times(1)).delete(any(ModelAndCategoryLink.class));
        verify(categoryRep, times(0)).delete(any(Category.class));
    }
    
    @Test
    void testRemoveCategoryByModelNameByManufacturerNameByCategoryName_shouldNotFoundException_whenTheCategoryNotExist() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";
        
        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Collections.emptyList());
        when(modelAndCategoryRep.existsById(new ModelAndCategoryLink(model, category).getId())).thenReturn(true);
        when(carRep.findAllByCategory(category)).thenReturn(Collections.emptyList());
        when(modelAndCategoryRep.findAllByCategory(category)).thenReturn(Arrays.asList(new ModelAndCategoryLink(model, category)));
        
        assertThrows(ResourceNotFoundException.class, () -> {categoryServ.removeCategoryByModelNameByManufacturerNameByCategoryName("Volvo", "XC70", "SUV");});
        
        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(0)).existsById(any(ModelAndCategoryLinkKey.class));
        verify(carRep, times(0)).findAllByCategory(any(Category.class));
        verify(modelAndCategoryRep, times(0)).delete(any(ModelAndCategoryLink.class));
        verify(categoryRep, times(0)).delete(any(Category.class));
    }
    
    @Test
    void testRemoveCategoryByModelNameByManufacturerNameByCategoryName_shouldNotFoundException_whenLinkTheCategoryNotExistAndtheCatigoryExist() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";
        
        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(modelAndCategoryRep.existsById(new ModelAndCategoryLink(model, category).getId())).thenReturn(false);
        when(carRep.findAllByCategory(category)).thenReturn(Collections.emptyList());
        when(modelAndCategoryRep.findAllByCategory(category)).thenReturn(Arrays.asList(new ModelAndCategoryLink(model, category)));
        
        assertThrows(ResourceNotFoundException.class, () -> {categoryServ.removeCategoryByModelNameByManufacturerNameByCategoryName("Volvo", "XC70", "SUV");});
        
        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(1)).existsById(any(ModelAndCategoryLinkKey.class));
        verify(carRep, times(0)).findAllByCategory(any(Category.class));
        verify(modelAndCategoryRep, times(0)).delete(any(ModelAndCategoryLink.class));
        verify(categoryRep, times(0)).delete(any(Category.class));
    }
    
    @Test
    void testRemoveCategoryByModelNameByManufacturerNameByCategoryName_shouldStillUsedException_whenLinkTheCategoryNotExistAndtheCatigoryExist() {
        Model model = new Model(6L, "XC70", new Manufacturer(2L, "Volvo"));
        Category category = new Category(3L, "SUV");
        String expected = "CategoryDto(id=3, name=SUV)";
        
        when(modelServ.retrieveEntityByManufacturerNameByModelName("Volvo", "XC70")).thenReturn(model);
        when(categoryRep.findByName(category.getName())).thenReturn(Arrays.asList(category));
        when(modelAndCategoryRep.existsById(new ModelAndCategoryLink(model, category).getId())).thenReturn(true);
        when(carRep.findAllByCategory(category)).thenReturn(Arrays.asList(new Car()));
        when(modelAndCategoryRep.findAllByCategory(category)).thenReturn(Arrays.asList(new ModelAndCategoryLink(model, category)));
        
        assertThrows(ResourceIsStillUsedException.class, () -> {categoryServ.removeCategoryByModelNameByManufacturerNameByCategoryName("Volvo", "XC70", "SUV");});
        
        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(categoryRep, times(1)).findByName(any(String.class));
        verify(modelAndCategoryRep, times(1)).existsById(any(ModelAndCategoryLinkKey.class));
        verify(carRep, times(2)).findAllByCategory(any(Category.class));
        verify(modelAndCategoryRep, times(0)).delete(any(ModelAndCategoryLink.class));
        verify(categoryRep, times(0)).delete(any(Category.class));
    }
        
}
