package co.vinod.service;

import co.vinod.dto.CustomerDto;
import co.vinod.entity.Customer;
import co.vinod.exception.ResourceNotFoundException;
import co.vinod.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customerDto.getEmail());
        }
        Customer customer = mapToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDto(savedCustomer);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!existingCustomer.getEmail().equals(customerDto.getEmail()) && 
                customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customerDto.getEmail());
        }

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPhone(customerDto.getPhone());
        existingCustomer.setCity(customerDto.getCity());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapToDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(existingCustomer);
    }

    @Override
    public List<CustomerDto> getCustomersByCity(String city) {
        return customerRepository.findByCity(city).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .city(customer.getCity())
                .build();
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        return Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .city(customerDto.getCity())
                .build();
    }
}
