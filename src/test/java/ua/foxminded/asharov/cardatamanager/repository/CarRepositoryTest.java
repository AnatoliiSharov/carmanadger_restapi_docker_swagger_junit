package ua.foxminded.asharov.cardatamanager.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.asharov.cardatamanager.dto.SearchObject;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.specifications.CarSpecificationBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest {
    
    @Autowired
    CarRepository carRep;
    CarSpecificationBuilder carSpecificationBuilder = new CarSpecificationBuilder();
    
    @ParameterizedTest
    @MethodSource("provideDataForTestFindAllSpecificationOfTPageable")
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindAllSpecificationOfTPageable(String[] searchedCategories, String[] searchedManufacturers, String[] searchedModels, 
            String searchedMinYears, String searchedMaxYears, String expected) {
        SearchObject searchObject = new SearchObject();
        searchObject.setCategory(searchedCategories);
        searchObject.setManufacturer(searchedManufacturers);
        searchObject.setModel(searchedModels);
        searchObject.setMaxYear(searchedMaxYears);
        searchObject.setMinYear(searchedMinYears);
        
        Specification<Car> spec = carSpecificationBuilder.toSpecification(searchObject);
        List<Car> actual = StreamSupport.stream(carRep.findAll(spec).spliterator(), false).collect(Collectors.toList());
        
        assertEquals(expected, actual.toString());
    }

    private static Stream<Arguments> provideDataForTestFindAllSpecificationOfTPageable() {
//String[] searchedCategories, String[] searchedManufacturers, String[] searchedModels, String searchedMinYears, String searchedMaxYears, String expected)"
        return Stream.of(
                Arguments.of(new String[]{"Sedan"}, null, null, null, null,
                "[Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan))]"),
                Arguments.of(new String[]{"Sedan", "Wagon"}, null, null, null, null,
                        "[Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=7, objectId=jwdXFQAFx6, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=4, year=2005), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=4, name=Wagon)), "
                        + "Car(id=8, objectId=lzF2TNp55g, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=3, year=2004), model=Model(id=2, name=6 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(null, new String[]{"BMW"}, null, null, null,
                        "[Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=8, objectId=lzF2TNp55g, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=3, year=2004), model=Model(id=2, name=6 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(null, new String[]{"BMW", "Volvo"}, null, null, null,
                        "[Car(id=2, objectId=hqczihz66a, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=2, year=2019), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=3, name=SUV)), "
                        + "Car(id=3, objectId=Kb9b2493oy, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=2, year=2019), model=Model(id=5, name=XC40, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=3, name=SUV)), "
                        + "Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=7, objectId=jwdXFQAFx6, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=4, year=2005), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=4, name=Wagon)), "
                        + "Car(id=8, objectId=lzF2TNp55g, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=3, year=2004), model=Model(id=2, name=6 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(null, null, new String[]{"XC70"}, null, null,
                        "[Car(id=2, objectId=hqczihz66a, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=2, year=2019), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=3, name=SUV)), "
                        + "Car(id=7, objectId=jwdXFQAFx6, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=4, year=2005), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(null, null, new String[]{"XC70", "Tundra Double Cab"}, null, null,
                        "[Car(id=1, objectId=8ZtzCENmXb, manufacturer=Manufacturer(id=1, name=Toyota), year=Year(id=2, year=2019), model=Model(id=6, name=Tundra Double Cab, manufacturer=Manufacturer(id=1, name=Toyota)), category=Category(id=2, name=Pickup)), "
                                + "Car(id=2, objectId=hqczihz66a, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=2, year=2019), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=3, name=SUV)), "
                                + "Car(id=6, objectId=8ZtPCENmXb, manufacturer=Manufacturer(id=1, name=Toyota), year=Year(id=4, year=2005), model=Model(id=6, name=Tundra Double Cab, manufacturer=Manufacturer(id=1, name=Toyota)), category=Category(id=2, name=Pickup)), "
                                + "Car(id=7, objectId=jwdXFQAFx6, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=4, year=2005), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(null, null, null, "2017", null,
                        "[Car(id=1, objectId=8ZtzCENmXb, manufacturer=Manufacturer(id=1, name=Toyota), year=Year(id=2, year=2019), model=Model(id=6, name=Tundra Double Cab, manufacturer=Manufacturer(id=1, name=Toyota)), category=Category(id=2, name=Pickup)), "
                        + "Car(id=2, objectId=hqczihz66a, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=2, year=2019), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=3, name=SUV)), "
                        + "Car(id=3, objectId=Kb9b2493oy, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=2, year=2019), model=Model(id=5, name=XC40, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=3, name=SUV)), "
                        + "Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan))]"),
                Arguments.of(null, null, null, null, "2017",
                        "[Car(id=6, objectId=8ZtPCENmXb, manufacturer=Manufacturer(id=1, name=Toyota), year=Year(id=4, year=2005), model=Model(id=6, name=Tundra Double Cab, manufacturer=Manufacturer(id=1, name=Toyota)), category=Category(id=2, name=Pickup)), "
                        + "Car(id=7, objectId=jwdXFQAFx6, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=4, year=2005), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=4, name=Wagon)), "
                        + "Car(id=8, objectId=lzF2TNp55g, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=3, year=2004), model=Model(id=2, name=6 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(null, null, null, "2004", "2019",
                        "[Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                        + "Car(id=6, objectId=8ZtPCENmXb, manufacturer=Manufacturer(id=1, name=Toyota), year=Year(id=4, year=2005), model=Model(id=6, name=Tundra Double Cab, manufacturer=Manufacturer(id=1, name=Toyota)), category=Category(id=2, name=Pickup)), "
                        + "Car(id=7, objectId=jwdXFQAFx6, manufacturer=Manufacturer(id=2, name=Volvo), year=Year(id=4, year=2005), model=Model(id=1, name=XC70, manufacturer=Manufacturer(id=2, name=Volvo)), category=Category(id=4, name=Wagon))]"),
                Arguments.of(new String[]{"Sedan"}, new String[]{"BMW"}, null, null, null,
                        "[Car(id=4, objectId=SLYFpQdCVH, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=3, name=7 Series, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan)), "
                                + "Car(id=5, objectId=WHCTL8PKc2, manufacturer=Manufacturer(id=3, name=BMW), year=Year(id=1, year=2018), model=Model(id=4, name=M3, manufacturer=Manufacturer(id=3, name=BMW)), category=Category(id=1, name=Sedan))]")
                );
    }

}
