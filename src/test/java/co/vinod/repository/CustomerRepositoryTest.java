package co.vinod.repository;

import co.vinod.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("1234567890")
                .city("New York")
                .build();
    }

    @Test
    public void testSaveCustomer() {
        Customer savedCustomer = customerRepository.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByEmail() {
        customerRepository.save(customer);
        Optional<Customer> foundCustomer = customerRepository.findByEmail("john@example.com");
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindByCity() {
        customerRepository.save(customer);
        List<Customer> customers = customerRepository.findByCity("New York");
        assertThat(customers).isNotEmpty();
        assertThat(customers.size()).isEqualTo(1);
    }
}
