package ua.foxminded.asharov.cardatamanager.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;
import ua.foxminded.asharov.cardatamanager.secure.SecurityConfig;
import ua.foxminded.asharov.cardatamanager.service.ModelService;

@WebMvcTest(controllers = { ManufacturersModelController.class, ControllerExceptionHandler.class })
@Import(SecurityConfig.class)
class ManufacturersModelControllerTest {
    @MockBean
    JwtDecoder jwtDecoder;
    @MockBean
    ModelService modelServ;
    @Autowired
    ManufacturersModelController manufacrtrersModelControl;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("test")
    void testShowManufacturerAllModels() throws Exception {
        Manufacturer manafacturer = new Manufacturer(3L, "BMW");
        List<ModelDto> modelDtos = Arrays.asList(new ModelDto(2L, "6 Series", manafacturer.getId()),
                new ModelDto(3L, "7 Series", manafacturer.getId()), new ModelDto(4L, "M3", manafacturer.getId()));
        String expectedJson = new ObjectMapper().writeValueAsString(modelDtos);

        when(modelServ.retrieveByManufacturerName(manafacturer.getName())).thenReturn(modelDtos);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/manufacturers/{manufacturer}/models",
                        manafacturer.getName()))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().json(expectedJson));
        verify(modelServ, times(1)).retrieveByManufacturerName(any(String.class));
    }

    @Test
    @WithMockUser("test")
    void testShowManufacturerModel() throws Exception {
        ModelDto modelDto = new ModelDto(3L, "7 Series", 3L);
        ManufacturerDto manufacturerDto = new ManufacturerDto(3L, "BMW");
        String expectedJsom = new ObjectMapper().writeValueAsString(modelDto);

        when(modelServ.retrieveByManufacturerNameByModelName(manufacturerDto.getName(), modelDto.getName()))
                .thenReturn(modelDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/manufacturers/{manufacturer}/models/{model}",
                        manufacturerDto.getName(), modelDto.getName()))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().json(expectedJsom));
        verify(modelServ, times(1)).retrieveByManufacturerNameByModelName(any(String.class), any(String.class));
    }

    @Test
    @WithMockUser("test")
    void testCreateNewModelWithManufacturer() throws Exception {
        ModelDto modelDto = new ModelDto(null, "7 Series", 3L);
        ManufacturerDto manufacturerDto = new ManufacturerDto(3L, "BMW");
        String paramJson = new ObjectMapper().writeValueAsString(modelDto);

        when(modelServ.enter(manufacturerDto.getName(), modelDto)).thenReturn(new ModelDto(3L, "7 Series", 3L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/manufacturers/{manufacturer}/models/{model}", manufacturerDto.getName(), modelDto.getName())
                .content(paramJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost/api/v1/manufacturers/BMW/models/7%2520Series/7%20Series",
                response.getHeader(HttpHeaders.LOCATION));
        
        verify(modelServ, times(1)).enter(any(String.class), any(ModelDto.class));
    }

    @Test
    @WithMockUser("test")
    void testUpdateModelWithManufacturer() throws Exception {
        ModelDto modelDto = new ModelDto(3L, "7 Series", 3L);
        ManufacturerDto manufacturerDto = new ManufacturerDto(3L, "BMW");
        String paramJson = new ObjectMapper().writeValueAsString(modelDto);

        when(modelServ.enter(manufacturerDto.getName(), modelDto)).thenReturn(modelDto);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/manufacturers/{manufacturer}/models/", manufacturerDto.getName())
                        .content(paramJson)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(paramJson));

        verify(modelServ, times(1)).enter(any(String.class), any(ModelDto.class));
    }

    @Test
    @WithMockUser("test")
    void testDeleteManufacturerModel() throws Exception {
        ModelDto modelDto = new ModelDto(3L, "7 Series", 3L);
        ManufacturerDto manufacturerDto = new ManufacturerDto(3L, "BMW");

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/manufacturers/{manufacturer}/models/{model}",
                manufacturerDto.getName(), modelDto.getName())).andExpect(status().isOk());
        verify(modelServ, times(1)).removeByManufacturerNameByModelName(any(String.class), any(String.class));
    }

}
