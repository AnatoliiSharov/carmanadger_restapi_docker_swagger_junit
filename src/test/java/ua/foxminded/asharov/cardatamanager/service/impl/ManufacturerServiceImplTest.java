package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.foxminded.asharov.cardatamanager.dto.ManufacturerDto;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.exception.ResourceAlreadyExistsException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.service.ManufacturerService;

@SpringBootTest(classes = { ManufacturerServiceImpl.class })
class ManufacturerServiceImplTest {

    @Autowired
    ManufacturerService manufacturerServ;
    @MockBean
    ManufacturerRepository manufacturerRep;

    @Test
    void testRetrieveAll() {
        List<Manufacturer> allManufacturers = Arrays.asList(new Manufacturer(1L, "Toyota"),
                new Manufacturer(2L, "Volvo"), new Manufacturer(3L, "BMW"));
        String expected = "[ManufacturerDto(id=1, name=Toyota), ManufacturerDto(id=2, name=Volvo), ManufacturerDto(id=3, name=BMW)]";

        when(manufacturerRep.findAll()).thenReturn(allManufacturers);
        assertEquals(expected, manufacturerServ.retrieveAll().toString());
        verify(manufacturerRep, times(1)).findAll();
    }

    @Test
    void testRetrieveByName_shouldManufacturerDto_whenNameExistent() {
        String manufacturerName = "BMW";
        String expected = "ManufacturerDto(id=3, name=BMW)";

        when(manufacturerRep.findByName(manufacturerName)).thenReturn(Arrays.asList(new Manufacturer(3L, "BMW")));
        assertEquals(expected, manufacturerServ.retrieveByName(manufacturerName).toString());
        verify(manufacturerRep, times(1)).findByName(manufacturerName);
    }

    @Test
    void testRetrieveByName_shouldTrowNotFoundException_whenNameNonExistent() {
        String manufacturerName="nonExistentName";

        when(manufacturerRep.findByName(manufacturerName)).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class,()->{manufacturerServ.retrieveByName(manufacturerName);});
        verify(manufacturerRep,times(1)).findByName(manufacturerName);
    }

    @Test
    void testEnter_shouldSave_whenNewEntityAndNewNameNonExist() {
        String expected = "ManufacturerDto(id=2, name=Volvo)";

        when(manufacturerRep.existsByName("Volvo")).thenReturn(false);
        when(manufacturerRep.save(new Manufacturer(null, "Volvo"))).thenReturn(new Manufacturer(2L, "Volvo"));

        assertEquals(expected, manufacturerServ.enter(new ManufacturerDto(null, "Volvo")).toString());

        verify(manufacturerRep, times(1)).existsByName(any(String.class));
        verify(manufacturerRep, times(0)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).save(any(Manufacturer.class));
    }

    @Test
    void testEnter_shouldSave_whenExistenEntityAndIdRightAndNameNewNotExist() {
        String expected = "ManufacturerDto(id=2, name=Volvo)";

        when(manufacturerRep.findById(2L)).thenReturn(Optional.of(new Manufacturer(2L, "NotVolvo")));
        when(manufacturerRep.existsByName("Volvo")).thenReturn(false);
        when(manufacturerRep.save(new Manufacturer(2L, "Volvo"))).thenReturn(new Manufacturer(2L, "Volvo"));

        assertEquals(expected, manufacturerServ.enter(new ManufacturerDto(2L, "Volvo")).toString());

        verify(manufacturerRep, times(1)).existsByName(any(String.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).save(new Manufacturer(2L, "Volvo"));
    }

    @Test
    void testEnter_shouldOk_whenExistenEntityWithRightIdAndExistenName() {
        String expected = "ManufacturerDto(id=2, name=Volvo)";

        when(manufacturerRep.findById(2L)).thenReturn(Optional.of(new Manufacturer(2L, "NotVolvo")));
        when(manufacturerRep.existsByName("Volvo")).thenReturn(false);
        when(manufacturerRep.save(new Manufacturer(2L, "Volvo"))).thenReturn(new Manufacturer(2L, "Volvo"));

        assertEquals(expected, manufacturerServ.enter(new ManufacturerDto(2L, "Volvo")).toString());

        verify(manufacturerRep, times(1)).existsByName(any(String.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).save(any(Manufacturer.class));
    }

    @Test
    void testEnter_shouldThroweAlreadyExists_whenNewEntityAndNameAlreadyExistinBase() {
        ManufacturerDto manufacturerDto = new ManufacturerDto(null, "Volvo");
        
        when(manufacturerRep.existsByName("Volvo")).thenReturn(true);
        when(manufacturerRep.save(new Manufacturer(null,"Volvo"))).thenReturn(new Manufacturer(2L,"Volvo"));
        
        assertThrows(ResourceAlreadyExistsException.class,() -> {manufacturerServ.enter(manufacturerDto);});
        
        verify(manufacturerRep,times(1)).existsByName(any(String.class));
        verify(manufacturerRep,times(0)).findById(any(Long.class));
        verify(manufacturerRep,times(0)).save(any(Manufacturer.class));
    }

    @Test
    void testEnter_shouldTrowNotExist_whenExistenEntityAndIdIsWrong() {
        ManufacturerDto manufacturerDto = new ManufacturerDto(2L, "Volvo");
        
        when(manufacturerRep.findById(9999L)).thenReturn(Optional.empty());
        when(manufacturerRep.existsByName("Volvo")).thenReturn(true);
        when(manufacturerRep.save(new Manufacturer(2L,"Volvo"))).thenReturn(new Manufacturer(2L,"Volvo"));

        assertThrows(ResourceNotFoundException.class, () -> {manufacturerServ.enter(manufacturerDto);});

        verify(manufacturerRep,times(0)).existsByName(any(String.class));
        verify(manufacturerRep,times(1)).findById(any(Long.class));
        verify(manufacturerRep,times(0)).save(any(Manufacturer.class));
    }

    @Test
    void testRemoveByName_shouldRemoved_whenExistenName() {
        when(manufacturerRep.findByName("Volvo")).thenReturn(Arrays.asList(new Manufacturer(2L,"Volvo")));
        
        manufacturerServ.removeByName("Volvo");
        
        verify(manufacturerRep, times(1)).findByName("Volvo");
    }
    
    @Test
    void testRemoveByName_shouldThrow_whenNonExistenName() {
        ManufacturerDto manufacturerDto = new ManufacturerDto(2L, "Volvo");
        
        when(manufacturerRep.findByName("Volvo")).thenReturn(Collections.emptyList());
        
        assertThrows(ResourceNotFoundException.class,() -> {manufacturerServ.enter(manufacturerDto);});
        
        verify(manufacturerRep, times(0)).findByName("Volvo");
    }

}
