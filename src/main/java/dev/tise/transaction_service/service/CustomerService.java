package dev.tise.transaction_service.service;

import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.request.CreateCustomerRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomerService {

    Customer createNewCustomer(String name, LocalDate dob, String state, String email);
    String updateCustomer(Long id, CreateCustomerRequest request);
    String deleteCustomer(Long id);
    String insertData(MultipartFile file);
    Customer findCustomerById(Long id);
    Optional<Customer> findByEmail(String email);



}
