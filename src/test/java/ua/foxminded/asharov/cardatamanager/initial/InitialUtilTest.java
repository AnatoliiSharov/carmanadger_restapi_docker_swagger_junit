package ua.foxminded.asharov.cardatamanager.initial;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.foxminded.asharov.cardatamanager.service.InitialService;

@SpringBootTest(classes = { InitialUtil.class, InitialService.class })
class InitialUtilTest {
    @MockBean
    InitialService initialServ;
    @Autowired
    InitialUtil initUtil;
    
    @Test
    void testLoadResource() {
        String filePlasePath = "testfile.csv";
        List<String[]> expected = Arrays.asList(
                new String[]{"PROCESS", "1", "ZRgPP9dBMm","Audi","2020","Q3","SUV"},      
                new String[]{"PROCESS", "2","cptB1C1NSL","Chevrolet","2020","Malibu","Sedan"},
                new String[]{"PROCESS", "3","ElhqsRZDnP","Cadillac","2020","Escalade ESV","SUV"},
                new String[]{"PROCESS", "4","LUzyWMYJpW","Chevrolet","2020","Corvette","Coupe, Convertible"},
                new String[]{"PROCESS", "5","rDkHakOBKP","Acura","2020","RLX","Sedan"},
                new String[]{"PROCESS", "6","1JARpJ2AUB","Chevrolet","2020","Silverado 2500 HD Crew Cab","Pickup"},
                new String[]{"PROCESS", "7","7G1VT2pSNO","BMW","2020","3 Series","Sedan"},
                new String[]{"PROCESS", "8","4q7L9FAU2S","Chrysler","2020","Pacifica","Van/Minivan"},
                new String[]{"PROCESS", "9","jpOC5zs3jx","Chevrolet","2020","Colorado Crew Cab","Pickup"}
                ); 
        assertEquals(Arrays.asList(expected.get(2)), Arrays.asList(initUtil.loadResource(filePlasePath).get(2)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "PROCESS, 1, ZRgPP9dBMm, Audi, 2020, Q3, SUV, APPROVAL",
            "PROCESS, 1, ZRgPP9dBMmAAAA, Audi, 2020, Q3, SUV, DENIED",
    })
    void testDataTreatment(String one, String two, String three, String four, String five, String six, String seven, String expect) {
        String[] initDate = {"PROCESS", "1", "ZRgPP9dBMm","Audi","2020","Q3","SUV"};
        String[] expectDate = initDate;
        
        expectDate[0] = expect;
        assertEquals(Arrays.asList(expectDate), Arrays.asList(initUtil.dataTreatment(initDate)));
    }

//    @Test
    void testPrintOut() {
        fail("Not yet implemented");
    }
    
    @ParameterizedTest
    @CsvSource({ "'', false", 
        "'AAAAaAAA9', false", 
        "'9AAAAaAAAA', true", 
        "'9 AAAaAAAA', false", 
            "'AAAAAaAAAA9', false" })
    void testIsOkobjectIdName(String in, Boolean expected) {
        assertEquals(expected, initUtil.isOkObjectIdName(in));
    }

    @ParameterizedTest
    @CsvSource({ "'', false", 
        "' ', false", 
        "'A', false", 
        "'A ', true", 
        "'A1', true", 
        "'A-1 .b_', true", 
        "'   ', false" })
    void testIsOkCategoryName(String in, Boolean expected) {
        assertEquals(expected, initUtil.isOkCategoryName(in));
        ;
    }

    @ParameterizedTest 
    @CsvSource({ "'', false", "'    ', false", "'AAA9', false", "'2000', true", "'+000', false", "'20000', false" })
    void testIsOkYearName(String in, Boolean expected) {
        assertEquals(expected, initUtil.isOkYearName(in));
    }

    @ParameterizedTest
    @CsvSource({ "'', false", 
        "' ', false", 
        "'A', false", 
        "'A ', true", 
        "'A1', true", 
        "'A-1 .b_', true", 
        "'   ', false" })
    void testIsOkModelName(String in, Boolean expected) {
        assertEquals(expected, initUtil.isOkModelName(in));
    }

    @ParameterizedTest
    @CsvSource({ "'', false", 
        "' ', false", 
        "'A', false", 
        "'A ', true", 
        "'A1', true", 
        "'A-1 .b_', true", 
        "'   ', false" })
    void testIsOkManufacturerName(String in, Boolean expected) {
        assertEquals(expected, initUtil.isOkManufacturerName(in));
    }
    
}
