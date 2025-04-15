package dev.tise.transaction_service.service;

import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.request.CreateCustomerRequest;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    Customer createNewCustomer(String name, int age, String state, String email);
    String updateCustomer(Long id, CreateCustomerRequest request);
    String deleteCustomer(Long id);
    String insertData(MultipartFile file);
    Customer findCustomerById(Long id);



}
