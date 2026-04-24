package co.vinod.service;

import co.vinod.dto.CustomerDto;
import java.util.List;

public interface CustomerService {
    
    CustomerDto createCustomer(CustomerDto customerDto);
    
    CustomerDto getCustomerById(Long id);
    
    List<CustomerDto> getAllCustomers();
    
    CustomerDto updateCustomer(Long id, CustomerDto customerDto);
    
    void deleteCustomer(Long id);
    
    List<CustomerDto> getCustomersByCity(String city);
}
