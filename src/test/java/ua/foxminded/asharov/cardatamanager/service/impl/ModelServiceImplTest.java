package ua.foxminded.asharov.cardatamanager.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.foxminded.asharov.cardatamanager.dto.ModelDto;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.exception.ResourceAlreadyExistsException;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;
import ua.foxminded.asharov.cardatamanager.repository.ManufacturerRepository;
import ua.foxminded.asharov.cardatamanager.repository.ModelRepository;

@SpringBootTest(classes = { ModelServiceImpl.class })
class ModelServiceImplTest {

    @MockBean
    ManufacturerRepository manufacturerRep;
    @MockBean
    ModelRepository modelRep;
    @Autowired
    ModelServiceImpl modelServ;

    @Test
    void testRetrieveByManufacturerName_shouldOk_whenManufactorExist() {
        List<Model> models = Arrays.asList(
                Model.builder().id(2L).name("6 Series").manufacturer(new Manufacturer(3L, "BMW")).build(),
                Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW")).build(),
                Model.builder().id(4L).name("M3").manufacturer(new Manufacturer(3L, "BMW")).build());
        String expected = "[ModelDto(id=2, name=6 Series, manufacturerId=3), ModelDto(id=3, name=7 Series, manufacturerId=3), ModelDto(id=4, name=M3, manufacturerId=3)]";

        when(manufacturerRep.findByName("BMW")).thenReturn(Arrays.asList(new Manufacturer(3L, "BMW")));
        when(modelRep.findByManufacturer(new Manufacturer(3L, "BMW"))).thenReturn(models);
        assertEquals(expected, modelServ.retrieveByManufacturerName("BMW").toString());
        verify(manufacturerRep, times(1)).findByName(any(String.class));
        verify(modelRep, times(1)).findByManufacturer(any(Manufacturer.class));
    }

    @Test
    void testRetrieveByManufacturerName_shouldThrowNotFoundException_whenTheManufactorNotExist() {
        when(manufacturerRep.findByName("Non Existen Manufacturer")).thenReturn(Collections.emptyList());
        
        assertThrows(ResourceNotFoundException.class,()->{modelServ.retrieveByManufacturerName("Non Existen Manufacturer");});
        
        verify(manufacturerRep,times(1)).findByName(any(String.class));verify(modelRep,times(0)).findByManufacturer(any(Manufacturer.class));
    }
    
    @Test
    void testEnter_shouldOk_whenOldModelManufactorExist() {
        String manufacturerName = "BMW";
        ModelDto modelDto = new ModelDto(3L, "7 Series", 3L);
        Model model = new Model(3L, "7 Series", new Manufacturer(3L, "BMW"));
        Manufacturer manufacturer = new Manufacturer(3L, "BMW");
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";
        
        when(modelRep.findById(modelDto.getId())).thenReturn(Optional.of(model));
        when(manufacturerRep.findById(modelDto.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(modelRep.existsByNameAndManufacturer(modelDto.getName(), manufacturer)).thenReturn(true);
        when(modelRep.save(new Model(3L, "7 Series", new Manufacturer(3L, "BMW")))).thenReturn(model);
        
        assertEquals(expected, modelServ.enter(manufacturerName, modelDto).toString());
        
        verify(modelRep, times(1)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(modelRep, times(1)).existsByNameAndManufacturer(any(String.class), any(Manufacturer.class));
        verify(modelRep, times(1)).save(any(Model.class));
    }

    @Test
    void testEnter_shouldOk_whenNewModelManufactorExist() {
        String manufacturerName = "BMW";
        ModelDto modelDto = new ModelDto(null, "7 Series", 3L);
        
        Model model = new Model(3L, "7 Series", new Manufacturer(3L, "BMW"));
        Manufacturer manufacturer = new Manufacturer(3L, "BMW");
        
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";
        
        when(modelRep.findById(3L)).thenReturn(Optional.of(model));
        when(manufacturerRep.findById(modelDto.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(modelRep.existsByNameAndManufacturer(modelDto.getName(), manufacturer)).thenReturn(false);
        when(modelRep.save(new Model(null, "7 Series", new Manufacturer(3L, "BMW")))).thenReturn(model);
        
        assertEquals(expected, modelServ.enter(manufacturerName, modelDto).toString());
        
        verify(modelRep, times(0)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(modelRep, times(1)).existsByNameAndManufacturer(any(String.class), any(Manufacturer.class));
        verify(modelRep, times(1)).save(any(Model.class));
    }
    
    @Test
    void testEnter_shouldThrowNotFoundException_whenModelNotExist() {
        String manufacturerName = "BMW";
        ModelDto modelDto = new ModelDto(999L, "7 Series", 3L);
        
        Model model = new Model(3L, "7 Series", new Manufacturer(3L, "BMW"));
        Manufacturer manufacturer = new Manufacturer(3L, "BMW");
        
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";
        
        when(modelRep.findById(3L)).thenReturn(Optional.empty());
        when(manufacturerRep.findById(modelDto.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(modelRep.existsByNameAndManufacturer(modelDto.getName(), manufacturer)).thenReturn(false);
        when(modelRep.save(new Model(null, "7 Series", new Manufacturer(3L, "BMW")))).thenReturn(model);
        
        
        assertThrows(ResourceNotFoundException.class,()->{modelServ.enter(manufacturerName, modelDto);});
        
        verify(modelRep, times(1)).findById(any(Long.class));
        verify(manufacturerRep, times(0)).findById(any(Long.class));
        verify(modelRep, times(0)).existsByNameAndManufacturer(any(String.class), any(Manufacturer.class));
        verify(modelRep, times(0)).save(any(Model.class));   
    }
    
    @Test
    void testEnter_shouldThrowNotFoundException_whenManufactorNotExist() {
        String manufacturerName = "BMW";
        ModelDto modelDto = new ModelDto(3L, "7 Series", 3L);
        
        Model model = new Model(3L, "7 Series", new Manufacturer(3L, "BMW"));
        Manufacturer manufacturer = new Manufacturer(3L, "BMW");
        
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";
        
        when(modelRep.findById(3L)).thenReturn(Optional.of(model));
        when(manufacturerRep.findById(modelDto.getManufacturerId())).thenReturn(Optional.empty());
        when(modelRep.existsByNameAndManufacturer(modelDto.getName(), manufacturer)).thenReturn(true);
        when(modelRep.save(new Model(3L, "7 Series", new Manufacturer(3L, "BMW")))).thenReturn(model);
        
        assertThrows(ResourceNotFoundException.class,()->{modelServ.enter(manufacturerName, modelDto);});
        
        verify(modelRep, times(1)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(modelRep, times(0)).existsByNameAndManufacturer(any(String.class), any(Manufacturer.class));
        verify(modelRep, times(0)).save(any(Model.class));   
    }
    
    @Test
    void testEnter_shouldThrowAlreadyExistException_whenNewModelNameAlreadyExist() {
        String manufacturerName = "BMW";
        ModelDto modelDto = new ModelDto(null, "7 Series", 3L);
        
        Model model = new Model(3L, "7 Series", new Manufacturer(3L, "BMW"));
        Manufacturer manufacturer = new Manufacturer(3L, "BMW");
        
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";
        
        when(modelRep.findById(3L)).thenReturn(Optional.of(model));
        when(manufacturerRep.findById(modelDto.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(modelRep.existsByNameAndManufacturer(modelDto.getName(), manufacturer)).thenReturn(true);
        when(modelRep.save(new Model(null, "7 Series", new Manufacturer(3L, "BMW")))).thenReturn(model);
        
        assertThrows(ResourceAlreadyExistsException.class,()->{modelServ.enter(manufacturerName, modelDto);});
        
        verify(modelRep, times(0)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(modelRep, times(1)).existsByNameAndManufacturer(any(String.class), any(Manufacturer.class));
        verify(modelRep, times(0)).save(any(Model.class));   
    }
    
    @Test
    void testEnter_shouldThrowAlreadyExistException_whenOldModelWontNameAlreadyExist() {
        String manufacturerName = "BMW";
        ModelDto modelDto = new ModelDto(3L, "9999 Series", 3L);
        
        Model model = new Model(3L, "7 Series", new Manufacturer(3L, "BMW"));
        Manufacturer manufacturer = new Manufacturer(3L, "BMW");
        
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";
        
        when(modelRep.findById(3L)).thenReturn(Optional.of(new Model(3L, "7 Series", new Manufacturer(3L, "BMW"))));
        when(manufacturerRep.findById(modelDto.getManufacturerId())).thenReturn(Optional.of(manufacturer));
        when(modelRep.existsByNameAndManufacturer(modelDto.getName(), manufacturer)).thenReturn(true);
        when(modelRep.save(new Model(null, "7 Series", new Manufacturer(3L, "BMW")))).thenReturn(new Model(3L, "7 Series", new Manufacturer(3L, "BMW")));
        
        assertThrows(ResourceAlreadyExistsException.class,()->{modelServ.enter(manufacturerName, modelDto);});
        
        verify(modelRep, times(1)).findById(any(Long.class));
        verify(manufacturerRep, times(1)).findById(any(Long.class));
        verify(modelRep, times(1)).existsByNameAndManufacturer(any(String.class), any(Manufacturer.class));
        verify(modelRep, times(0)).save(any(Model.class));   
    }

    @Test
    void testRemoveByManufacturerNameByModelName_shouldOk_whenTheManufacturerExist() {
        List<Model> models = Arrays
                .asList(Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW")).build());
        
        when(manufacturerRep.findByName("BMW")).thenReturn(Arrays.asList(new Manufacturer(3L, "BMW")));
        when(modelRep.findByNameAndManufacturer("7 Series", Arrays.asList(new Manufacturer(3L, "BMW")).get(0)))
                .thenReturn(models);
        
        modelServ.removeByManufacturerNameByModelName("BMW", "7 Series");

        verify(manufacturerRep, times(1)).findByName(any(String.class));
        verify(modelRep, times(1)).findByNameAndManufacturer(any(String.class), any(Manufacturer.class));
    }

    @ParameterizedTest
    @MethodSource("provideDataForTestRemoveByManufacturerNameByModelName_shouldThrowNotFoundException_whenTheManufactorNotExist")
    void testRemoveByManufacturerNameByModelName_shouldThrowNotFoundException_whenTheManufactorNotExist(
            int manufacturerTimes, int modelTimes, List<Manufacturer> manufacturers, List<Model> models) {
        when(manufacturerRep.findByName("BMW")).thenReturn(manufacturers);
        when(modelRep.findByNameAndManufacturer("7 Series",new Manufacturer(3L,"BMW"))).thenReturn(models);

        assertThrows(ResourceNotFoundException.class,()->{modelServ.removeByManufacturerNameByModelName("BMW", "7 Series");});

        verify(manufacturerRep,times(manufacturerTimes)).findByName(any(String.class));
        verify(modelRep,times(modelTimes)).findByNameAndManufacturer(any(String.class),any(Manufacturer.class));
    }

    private static Stream<Arguments> provideDataForTestRemoveByManufacturerNameByModelName_shouldThrowNotFoundException_whenTheManufactorNotExist() {
        return Stream.of(
                Arguments.of(1, 0, Collections.emptyList(),
                        Arrays.asList(Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW"))
                                .build())),
                Arguments.of(1, 1, Arrays.asList(new Manufacturer(3L, "BMW")), Collections.emptyList()));
    }
    
    @Test
    void testRetrieveByManufacturerNameByModelName_shouldOk_whenTheManufacturerExist() {
        List<Model> models = Arrays
                .asList(Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW")).build());
        String expected = "ModelDto(id=3, name=7 Series, manufacturerId=3)";

        when(manufacturerRep.findByName("BMW")).thenReturn(Arrays.asList(new Manufacturer(3L, "BMW")));
        when(modelRep.findByNameAndManufacturer("7 Series", Arrays.asList(new Manufacturer(3L, "BMW")).get(0)))
                .thenReturn(models);

        assertEquals(expected, modelServ.retrieveByManufacturerNameByModelName("BMW", "7 Series").toString());

        verify(manufacturerRep, times(1)).findByName(any(String.class));
        verify(modelRep, times(1)).findByNameAndManufacturer(any(String.class), any(Manufacturer.class));
    }

    @ParameterizedTest
    @MethodSource("provideDataForTestRetrieveByManufacturerNameByModelName_shouldOk_whenTheManufacturerExist")
    void testRetrieveByManufacturerNameByModelName_shouldThrowNotFoundException_whenTheManufactorNotExist(
            int manufacturerTimes, int modelTimes, List<Manufacturer> manufacturers, List<Model> models) {
        when(manufacturerRep.findByName("BMW")).thenReturn(manufacturers);
        when(modelRep.findByNameAndManufacturer("7 Series",new Manufacturer(3L,"BMW"))).thenReturn(models);

        assertThrows(ResourceNotFoundException.class,()->{modelServ.retrieveByManufacturerNameByModelName("BMW", "7 Series");});

        verify(manufacturerRep,times(manufacturerTimes)).findByName(any(String.class));
        verify(modelRep,times(modelTimes)).findByNameAndManufacturer(any(String.class),any(Manufacturer.class));
    }

    private static Stream<Arguments> provideDataForTestRetrieveByManufacturerNameByModelName_shouldOk_whenTheManufacturerExist() {
        return Stream.of(
                Arguments.of(1, 0, Collections.emptyList(),
                        Arrays.asList(Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW"))
                                .build())),
                Arguments.of(1, 1, Arrays.asList(new Manufacturer(3L, "BMW")), Collections.emptyList()));
    }

    @Test
    void testRetrieveEntityByManufacturerNameByModelName_Ok_whenAllOk() {
        List<Model> models = Arrays
                .asList(Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW")).build());
        String expected = "Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW))";

        when(manufacturerRep.findByName("BMW")).thenReturn(Arrays.asList(new Manufacturer(3L, "BMW")));
        when(modelRep.findByNameAndManufacturer("7 Series", Arrays.asList(new Manufacturer(3L, "BMW")).get(0)))
                .thenReturn(models);
        
        assertEquals(expected, modelServ.retrieveEntityByManufacturerNameByModelName("BMW", "7 Series").toString());

        verify(manufacturerRep, times(1)).findByName(any(String.class));
        verify(modelRep, times(1)).findByNameAndManufacturer(any(String.class), any(Manufacturer.class));
    }

    @ParameterizedTest
    @MethodSource("provideDataForTestRetrieveEntityByManufacturerNameByModelName_shouldThrowNotFoundException")
    void testRetrieveEntityByManufacturerNameByModelName_shouldThrowNotFoundException_whenTheManufactorNotExist(
            int manufacturerTimes, int modelTimes, List<Manufacturer> manufacturers, List<Model> models) {

        when(manufacturerRep.findByName("BMW")).thenReturn(manufacturers);when(modelRep.findByNameAndManufacturer("7 Series",new Manufacturer(3L,"BMW"))).thenReturn(models);

        assertThrows(ResourceNotFoundException.class,()->{modelServ.retrieveEntityByManufacturerNameByModelName("BMW", "7 Series");});

        verify(manufacturerRep,times(manufacturerTimes)).findByName(any(String.class));verify(modelRep,times(modelTimes)).findByNameAndManufacturer(any(String.class),any(Manufacturer.class));
    }

    private static Stream<Arguments> provideDataForTestRetrieveEntityByManufacturerNameByModelName_shouldThrowNotFoundException() {
        return Stream.of(
                Arguments.of(1, 0, Collections.emptyList(),
                        Arrays.asList(Model.builder().id(3L).name("7 Series").manufacturer(new Manufacturer(3L, "BMW"))
                                .build())),
                Arguments.of(1, 1, Arrays.asList(new Manufacturer(3L, "BMW")), Collections.emptyList()));
    }

}
