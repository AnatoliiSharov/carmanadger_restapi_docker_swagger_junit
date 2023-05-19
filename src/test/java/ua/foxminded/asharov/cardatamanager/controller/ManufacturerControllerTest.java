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
import ua.foxminded.asharov.cardatamanager.secure.SecurityConfig;
import ua.foxminded.asharov.cardatamanager.service.ManufacturerService;

@WebMvcTest(controllers = { ManufacturerController.class, ControllerExceptionHandler.class })
@Import(SecurityConfig.class)
class ManufacturerControllerTest {
    @MockBean
    JwtDecoder jwtDecoder;
    @MockBean
    ManufacturerService manufacturerServ;
    @Autowired
    ManufacturerController manufacturerControl;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("test")
    void testFindAllManufacturers() throws Exception {
        List<ManufacturerDto> manufacturerDtos = Arrays.asList(new ManufacturerDto(1L, "Toyota"),
                new ManufacturerDto(2L, "Volvo"), new ManufacturerDto(3L, "BMW"));
        String expectedJson = new ObjectMapper().writeValueAsString(manufacturerDtos);

        when(manufacturerServ.retrieveAll()).thenReturn(manufacturerDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/manufacturers")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json(expectedJson));

        verify(manufacturerServ, times(1)).retrieveAll();
    }

    @Test
    @WithMockUser("test")
    void testFindManufacturerByName() throws Exception {
        String manufacturerName = "BMW";
        ManufacturerDto manufacturerDto = new ManufacturerDto(3L, "BMW");
        String expectedJson = new ObjectMapper().writeValueAsString(manufacturerDto);

        when(manufacturerServ.retrieveByName(manufacturerName)).thenReturn(manufacturerDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/manufacturers/{manufacturer}",manufacturerDto.getName())).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json(expectedJson));

        verify(manufacturerServ, times(1)).retrieveByName(any(String.class));
    }

    @Test
    @WithMockUser("test")
    void testCreateNewManufacturer() throws Exception {
        String paramJson = new ObjectMapper().writeValueAsString(new ManufacturerDto(null, "Volvo"));
        when(manufacturerServ.enter(new ManufacturerDto(null, "Volvo"))).thenReturn(new ManufacturerDto(2L, "Volvo"));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/manufacturers")
                .content(paramJson)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost/api/v1/manufacturers/Volvo",
                response.getHeader(HttpHeaders.LOCATION));
 
        verify(manufacturerServ, times(1)).enter(any(ManufacturerDto.class));
    }

    @Test
    @WithMockUser("test")
    void testUpdateManufacturer() throws Exception {
        String paramJson = new ObjectMapper().writeValueAsString(new ManufacturerDto(2L, "Volvo"));

        when(manufacturerServ.enter(new ManufacturerDto(2L, "Volvo"))).thenReturn(new ManufacturerDto(2L, "Volvo"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/manufacturers/{manufacturer}", "Volvo").content(paramJson)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(paramJson));

        verify(manufacturerServ, times(1)).enter(any(ManufacturerDto.class));
    }

    @Test
    @WithMockUser("test")
    void testDeleteManufacturer() throws Exception {
        this.mockMvc.
        perform(MockMvcRequestBuilders.delete("/api/v1/manufacturers/{manufacturer}", "Volvo"))
        .andExpect(status().isOk());
        verify(manufacturerServ, times(1)).removeByName(any(String.class));

    }

    //this.mockMvc.perform(MockMvcRequestBuilders
    //.delete("/api/v1/manufacturers/{manufacturer}/models/{model}/{year}", manufacturerDto.getName(), modelDto.getName(), yearDto.getYear().toString()))
    //.andExpect(status().isOk());
}
