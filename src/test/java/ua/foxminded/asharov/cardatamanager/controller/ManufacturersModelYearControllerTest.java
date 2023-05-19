package ua.foxminded.asharov.cardatamanager.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.foxminded.asharov.cardatamanager.dto.ManufacturerDto;
import ua.foxminded.asharov.cardatamanager.dto.ModelDto;
import ua.foxminded.asharov.cardatamanager.dto.YearDto;
import ua.foxminded.asharov.cardatamanager.secure.SecurityConfig;
import ua.foxminded.asharov.cardatamanager.service.YearService;

@WebMvcTest(controllers = { ManufacturersModelYearController.class, ControllerExceptionHandler.class })
@Import(SecurityConfig.class)
class ManufacturersModelYearControllerTest {
    @MockBean
    JwtDecoder jwtDecoder;

    @MockBean
    YearService yearServ;
    @Autowired
    ManufacturersModelYearController manufacturersModelYearControl;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("test")
    void testFindAllYearsWithModelWithManufacturer() throws Exception {
        ManufacturerDto manufacturerDto = new ManufacturerDto(1L, "Toyota");
        ModelDto modelDto = new ModelDto(6L, "Tundra Double Cab", 1L);
        List<YearDto> listYearDtos = Arrays.asList(new YearDto(2L, 2019), new YearDto(4L, 2005));
        String expectedJson = new ObjectMapper().writeValueAsString(listYearDtos);

        when(yearServ.retrieveAllByModelByManufacturer(manufacturerDto.getName(), modelDto.getName()))
                .thenReturn(listYearDtos);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/manufacturers/{manufacturer}/models/{model}/years",
                        manufacturerDto.getName(), modelDto.getName()))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().json(expectedJson));

        verify(yearServ, times(1)).retrieveAllByModelByManufacturer(any(String.class), any(String.class));
    }

    @Test
    @WithMockUser("test")
    void testFindYearWithModelWithManufacturer() throws Exception {
        ManufacturerDto manufacturerDto = new ManufacturerDto(1L, "Toyota");
        ModelDto modelDto = new ModelDto(6L, "Tundra Double Cab", 1L);
        YearDto yearDto = new YearDto(2L, 2019);
        String expectedJson = new ObjectMapper().writeValueAsString(yearDto);

        when(yearServ.retrieveByModelByManufacturerByYearName(manufacturerDto.getName(), modelDto.getName(),
                yearDto.getYear().toString())).thenReturn(yearDto);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/manufacturers/{manufacturer}/models/{model}/{year}",
                        manufacturerDto.getName(), modelDto.getName(), yearDto.getYear().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(yearServ, times(1)).retrieveByModelByManufacturerByYearName(any(String.class), any(String.class),
                any(String.class));

    }
    
    @Test
    @WithMockUser("test")
    void testCreateYearsWithModelWithManufacturer() throws Exception {
        ManufacturerDto manufacturerDto = new ManufacturerDto(1L, "Toyota");
        ModelDto modelDto = new ModelDto(6L, "Tundra Double Cab", 1L);
        YearDto newYearDto = new YearDto(null, 2019);
        String paramJson = new ObjectMapper().writeValueAsString(newYearDto);
        
        when(yearServ.enterByManufacrureNameByModelNameByYearDto(manufacturerDto.getName(), modelDto.getName(), newYearDto))
        .thenReturn(new YearDto(2L, 2019));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/manufacturers/{manufacturer}/models/{model}/year", manufacturerDto.getName(), modelDto.getName())
                .content(paramJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost/api/v1/manufacturers/Toyota/models/Tundra%2520Double%2520Cab/year/2019",
                response.getHeader(HttpHeaders.LOCATION));
        
        verify(yearServ, times(1)).enterByManufacrureNameByModelNameByYearDto(manufacturerDto.getName(), modelDto.getName(), newYearDto);
    }

    @Test
    @WithMockUser("test")
    void testUpdateYearsWithModelWithManufacturer() throws Exception {
        ManufacturerDto manufacturerDto = new ManufacturerDto(1L, "Toyota");
        ModelDto modelDto = new ModelDto(6L, "Tundra Double Cab", 1L);
        YearDto yearDto = new YearDto(2L, 2019);
        String paramJson = new ObjectMapper().writeValueAsString(yearDto);
        
        when(yearServ.enterByManufacrureNameByModelNameByYearDto(manufacturerDto.getName(), modelDto.getName(), yearDto)).thenReturn(yearDto);
        
        this.mockMvc
        .perform(MockMvcRequestBuilders
                .put("/api/v1/manufacturers/{manufacturer}/models/{model}/year", manufacturerDto.getName(), modelDto.getName())
                .content(paramJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(paramJson));
        
        verify(yearServ, times(1)).enterByManufacrureNameByModelNameByYearDto(manufacturerDto.getName(), modelDto.getName(), yearDto);
    }

    @Test
    @WithMockUser("test")
    void testDeleteYearWithModelWithManufacturer() throws Exception {
        ManufacturerDto manufacturerDto = new ManufacturerDto(1L, "Toyota");
        ModelDto modelDto = new ModelDto(6L, "Tundra Double Cab", 1L);
        YearDto yearDto = new YearDto(2L, 2019);

this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/manufacturers/{manufacturer}/models/{model}/{year}", manufacturerDto.getName(), modelDto.getName(), yearDto.getYear().toString())).andExpect(status().isOk());       
        
        verify(yearServ, times(1)).removeYearByModelNameByManufacturerNameByYearName(manufacturerDto.getName(), modelDto.getName(), yearDto.getYear().toString());
    }

}
