package ua.foxminded.asharov.cardatamanager.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.foxminded.asharov.cardatamanager.dto.CarDto;
import ua.foxminded.asharov.cardatamanager.dto.SearchObject;
import ua.foxminded.asharov.cardatamanager.initial.PagePojoForTest;
import ua.foxminded.asharov.cardatamanager.secure.SecurityConfig;
import ua.foxminded.asharov.cardatamanager.service.CarService;

@WebMvcTest(controllers = { CarsController.class, ControllerExceptionHandler.class })
@Import(SecurityConfig.class)
class CarsControllerTest {
    @MockBean
    JwtDecoder jwtDecoder;
    @MockBean
    CarService carServ;
    @Autowired
    CarsController carsControl;
    @Autowired
    MockMvc mockMvc;

    @Test
    void testShowCars() throws Exception {
        SearchObject searchObject = SearchObject.builder().model(new String[] { "XC70", "XC40" })
                .manufacturer(new String[] { "Toyota", "Volvo" }).category(new String[] { "SUV" }).maxYear("2020")
                .minYear("2010").build();
        Sort sort = Sort.by(Sort.Order.asc("year"), Sort.Order.desc("model"));
        Pageable pageable = PageRequest.of(1, 5, sort);

        List<CarDto> carDtos = Arrays.asList(new CarDto(2L, "hqczihz66a", 2L, 2L, 1L, 3L));
        Page<CarDto> pageForMock = new PageImpl<CarDto>(carDtos);

        when(carServ.retrieveAll(pageable, searchObject)).thenReturn(pageForMock);//

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/custom")
                .queryParam("model", searchObject.getModel()).queryParam("manufacturer", searchObject.getManufacturer())
                .queryParam("category", searchObject.getCategory()).queryParam("minYear", searchObject.getMinYear())
                .queryParam("maxYear", searchObject.getMaxYear()).queryParam("sort", "year,asc")
                .queryParam("sort", "model,desc").queryParam("page", "1").queryParam("size", "5"))
                .andExpect(status().isOk()).andReturn();

        PagePojoForTest<CarDto> actual = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<PagePojoForTest<CarDto>>() {
                });

        assertEquals(pageForMock.getContent(), actual.getContent());

        verify(carServ, times(1)).retrieveAll(pageable, searchObject);
    }

}
