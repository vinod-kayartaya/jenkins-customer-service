package co.vinod.service;

import co.vinod.dto.CustomerDto;
import co.vinod.entity.Customer;
import co.vinod.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    public void setup() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("1234567890")
                .city("New York")
                .build();

        customerDto = CustomerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("1234567890")
                .city("New York")
                .build();
    }

    @Test
    public void testCreateCustomer() {
        given(customerRepository.existsByEmail(customerDto.getEmail())).willReturn(false);
        given(customerRepository.save(any(Customer.class))).willReturn(customer);

        CustomerDto savedCustomer = customerService.createCustomer(customerDto);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isEqualTo(1L);
    }

    @Test
    public void testGetCustomerById() {
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));

        CustomerDto foundCustomer = customerService.getCustomerById(1L);

        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getFirstName()).isEqualTo("John");
    }
}
