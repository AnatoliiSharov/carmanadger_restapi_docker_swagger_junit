package ua.foxminded.asharov.cardatamanager.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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

import ua.foxminded.asharov.cardatamanager.dto.YearDto;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLink;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLinkKey;
import ua.foxminded.asharov.cardatamanager.entity.Year;
import ua.foxminded.asharov.cardatamanager.exception.ResourceIsStillUsedException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.CarRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelAndYearLinkRepository;
import ua.foxminded.asharov.cardatamanager.repository.YearRepository;
import ua.foxminded.asharov.cardatamanager.service.ModelService;

@SpringBootTest(classes = { YearServiceImpl.class })
class YearServiceImplTest {
    @MockBean
    ModelAndYearLinkRepository modelAndYearRep;
    @MockBean
    YearRepository yearRep;
    @MockBean
    CarRepository carRep;
    @MockBean
    ModelService modelServ;
    @Autowired
    YearServiceImpl yearServ;

    @Test
    void testRetrieveAllByModelByManufacturer() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        String expected = "[YearDto(id=2, year=2019), YearDto(id=4, year=2005)]";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(modelAndYearRep.findByModel(model)).thenReturn(Arrays.asList(
                new ModelAndYearLink(model, new Year(2L, 2019)), new ModelAndYearLink(model, new Year(4L, 2005))));

        assertEquals(expected, yearServ.retrieveAllByModelByManufacturer("Toyota", "Tundra Double Cab").toString());

        verify(modelServ).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(modelAndYearRep).findByModel(any(Model.class));
    }

    @Test
    void testRetrieveByModelByManufacturerByYearName_shouldOk() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        String expected = "YearDto(id=2, year=2019)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2019)).thenReturn(Arrays.asList(new Year(2L, 2019)));
        when(modelAndYearRep.findById(new ModelAndYearLinkKey(6L, 2L)))
                .thenReturn(Optional.of(new ModelAndYearLink(model, new Year(2L, 2019))));

        assertEquals(expected,
                yearServ.retrieveByModelByManufacturerByYearName("Toyota", "Tundra Double Cab", "2019").toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(yearRep, times(1)).findByYear(any(Integer.class));
        verify(modelAndYearRep, times(1)).findById(any(ModelAndYearLinkKey.class));

    }

    @Test
    void testRetrieveByModelByManufacturerByYearName_shouldNotFoundException_whenYearNotExist() {
        Model model=new Model(6L,"Tundra Double Cab",new Manufacturer(1L,"Toyota"));

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota","Tundra Double Cab")).thenReturn(model);when(yearRep.findByYear(2019)).thenReturn(Collections.emptyList());when(modelAndYearRep.findById(new ModelAndYearLinkKey(6L,2L))).thenReturn(Optional.of(new ModelAndYearLink(model,new Year(2L,2019))));

        assertThrows(ResourceNotFoundException.class,()->{yearServ.retrieveByModelByManufacturerByYearName("Tundra Double Cab", "Toyota", "2019");});

        verify(modelServ,times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class),any(String.class));verify(yearRep,times(1)).findByYear(any(Integer.class));verify(modelAndYearRep,times(0)).findById(any(ModelAndYearLinkKey.class));
    }

    @Test
    void testRetrieveByModelByManufacturerByYearName_shouldNotFoundException_whenLinkYearAndModelNotExist() {
        Model model=new Model(6L,"Tundra Double Cab",new Manufacturer(1L,"Toyota"));

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota","Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2019)).thenReturn(Arrays.asList(new Year(2L,2019)))
        ;when(modelAndYearRep.findById(new ModelAndYearLinkKey(6L,2L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->{yearServ.retrieveByModelByManufacturerByYearName("Toyota","Tundra Double Cab", "2019");});

        verify(modelServ,times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class),any(String.class));verify(yearRep,times(1)).findByYear(any(Integer.class));verify(modelAndYearRep,times(1)).findById(any(ModelAndYearLinkKey.class));
    }

    @Test
    void testEnterByManufacrureNameByModelNameByYearDto_shouldOk_whenNewYearAndNewModalAndYearLink() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        YearDto enteringYearDto = new YearDto(null, 2018);
        Year creatingYear = new Year(999L, 2018);
        String expected = "YearDto(id=999, year=2018)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2018)).thenReturn(Collections.emptyList());
        when(yearRep.save(new Year(null, 2018))).thenReturn(creatingYear);
        when(modelAndYearRep.findById(new ModelAndYearLink(model, creatingYear).getId())).thenReturn(Optional.empty());
        when(modelAndYearRep.save(new ModelAndYearLink(model, creatingYear)))
                .thenReturn(new ModelAndYearLink(model, creatingYear));

        assertEquals(expected, yearServ
                .enterByManufacrureNameByModelNameByYearDto("Toyota", "Tundra Double Cab", enteringYearDto).toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(yearRep, times(1)).findByYear(any(Integer.class));
        verify(yearRep, times(1)).save(any(Year.class));
        verify(modelAndYearRep, times(1)).findById(any(ModelAndYearLinkKey.class));
        verify(modelAndYearRep, times(1)).save(any(ModelAndYearLink.class));
    }

    @Test
    void testEnterByManufacrureNameByModelNameByYearDto_shouldOk_whenExistingYearAndNewModalAndYearLink() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        YearDto enteringYearDto = new YearDto(2L, 2019);
        Year year = new Year(2L, 2019);
        String expected = "YearDto(id=2, year=2019)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2019)).thenReturn(Arrays.asList(year));
        when(yearRep.save(new Year(2L, 2019))).thenReturn(new Year(2L, 2019));
        when(modelAndYearRep.findById(new ModelAndYearLink(model, year).getId())).thenReturn(Optional.empty());
        when(modelAndYearRep.save(new ModelAndYearLink(model, year))).thenReturn(new ModelAndYearLink(model, year));

        assertEquals(expected, yearServ
                .enterByManufacrureNameByModelNameByYearDto("Toyota", "Tundra Double Cab", enteringYearDto).toString());

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(yearRep, times(1)).findByYear(any(Integer.class));
        verify(yearRep, times(0)).save(any(Year.class));
        verify(modelAndYearRep, times(1)).findById(any(ModelAndYearLinkKey.class));
        verify(modelAndYearRep, times(1)).save(any(ModelAndYearLink.class));
    }

    @Test
    void testRemoveYearByModelNameByManufacturerNameByYearName_shouldOk_whenExistingYearAndNobodyUseIt() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        YearDto enteringYearDto = new YearDto(2L, 2019);
        Year year = new Year(2L, 2019);
        String expected = "YearDto(id=2, year=2019)";

        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2019)).thenReturn(Arrays.asList(new Year(2L, 2019)));
        when(modelAndYearRep.existsById(new ModelAndYearLink(model, year).getId())).thenReturn(true);
        when(modelAndYearRep.findAllByYear(year)).thenReturn(Collections.emptyList());
        when(carRep.findAllByYear(year)).thenReturn(Collections.emptyList());

        yearServ.removeYearByModelNameByManufacturerNameByYearName("Toyota", "Tundra Double Cab", "2019");

        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(yearRep, times(1)).findByYear(any(Integer.class));
        verify(modelAndYearRep, times(1)).existsById(any(ModelAndYearLinkKey.class));
        verify(modelAndYearRep, times(1)).findAllByYear(any(Year.class));
        verify(carRep, times(1)).findAllByYear(any(Year.class));
        verify(modelAndYearRep, times(1)).delete(any(ModelAndYearLink.class));
        verify(yearRep, times(1)).delete(any(Year.class));
    }

    @Test
    void testRemoveYearByModelNameByManufacturerNameByYearName_shouldIsStillUsedException_whenCarsStillUseTheYear() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        YearDto enteringYearDto = new YearDto(2L, 2019);
        Year year = new Year(2L, 2019);
        String expected = "YearDto(id=2, year=2019)";
        
        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2019)).thenReturn(Arrays.asList(new Year(2L, 2019)));
        when(modelAndYearRep.existsById(new ModelAndYearLink(model, year).getId())).thenReturn(true);
        when(modelAndYearRep.findAllByYear(year)).thenReturn(Collections.emptyList());
        when(carRep.findAllByYear(year)).thenReturn(Arrays.asList(new Car()));
        
        assertThrows(ResourceIsStillUsedException.class,()->{yearServ.removeYearByModelNameByManufacturerNameByYearName("Toyota", "Tundra Double Cab", "2019");});
     
        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(yearRep, times(1)).findByYear(any(Integer.class));
        verify(modelAndYearRep, times(1)).existsById(any(ModelAndYearLinkKey.class));
        verify(modelAndYearRep, times(0)).findAllByYear(any(Year.class));
        verify(carRep, times(2)).findAllByYear(any(Year.class));
        verify(modelAndYearRep, times(0)).delete(any(ModelAndYearLink.class));
        verify(yearRep, times(0)).delete(any(Year.class));
    }
    
    @Test
    void testRemoveYearByModelNameByManufacturerNameByYearName_shouldNotFoundException_whenLinkYearAndModelNotExist() {
        Model model = new Model(6L, "Tundra Double Cab", new Manufacturer(1L, "Toyota"));
        YearDto enteringYearDto = new YearDto(2L, 2019);
        Year year = new Year(2L, 2019);
        String expected = "YearDto(id=2, year=2019)";
        
        when(modelServ.retrieveEntityByManufacturerNameByModelName("Toyota", "Tundra Double Cab")).thenReturn(model);
        when(yearRep.findByYear(2019)).thenReturn(Arrays.asList(new Year(2L, 2019)));
        when(modelAndYearRep.existsById(new ModelAndYearLink(model, year).getId())).thenReturn(false);
        when(modelAndYearRep.findAllByYear(year)).thenReturn(Collections.emptyList());
        when(carRep.findAllByYear(year)).thenReturn(Collections.emptyList());
        
        assertThrows(ResourceNotFoundException.class,()->{yearServ.removeYearByModelNameByManufacturerNameByYearName("Toyota", "Tundra Double Cab", "2019");});
        
        verify(modelServ, times(1)).retrieveEntityByManufacturerNameByModelName(any(String.class), any(String.class));
        verify(yearRep, times(1)).findByYear(any(Integer.class));
        verify(modelAndYearRep, times(1)).existsById(any(ModelAndYearLinkKey.class));
        verify(modelAndYearRep, times(0)).findAllByYear(any(Year.class));
        verify(carRep, times(0)).findAllByYear(any(Year.class));
        verify(modelAndYearRep, times(0)).delete(any(ModelAndYearLink.class));
        verify(yearRep, times(0)).delete(any(Year.class));
   
    }
    
    
}
