package co.vinod.controller;

import co.vinod.dto.CustomerDto;
import co.vinod.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        CustomerDto savedCustomer = CustomerDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        given(customerService.createCustomer(any(CustomerDto.class))).willReturn(savedCustomer);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        given(customerService.getCustomerById(1L)).willReturn(customerDto);

        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }
}
