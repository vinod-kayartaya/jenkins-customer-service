package co.vinod.integration;

import co.vinod.dto.CustomerDto;
import co.vinod.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateCustomer() {
        CustomerDto customerDto = CustomerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.integration@example.com")
                .city("New York")
                .build();

        ResponseEntity<CustomerDto> response = restTemplate.postForEntity("/api/customers", customerDto, CustomerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testGetAllCustomers() {
        CustomerDto customerDto = CustomerDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.integration@example.com")
                .build();
        restTemplate.postForEntity("/api/customers", customerDto, CustomerDto.class);

        ResponseEntity<CustomerDto[]> response = restTemplate.getForEntity("/api/customers", CustomerDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }
}
