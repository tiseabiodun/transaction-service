package dev.tise.transaction_service.controller;

import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.request.CreateCustomerRequest;
import dev.tise.transaction_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/get-customer-by-id/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        System.err.println("Fetching customer details for customer with id "+id);
        return new ResponseEntity<>(customerService.findCustomerById(id), HttpStatus.OK);
    }
    @PostMapping("/insert-from-file")
    public ResponseEntity<String> insertCustomers(@RequestParam("file") MultipartFile file) {
        System.err.println("in coming request for create customer :: " + toString());
        String response = customerService.insertData(file);
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-customer")
    public ResponseEntity<String> updateCustomer(@RequestParam Long id, @RequestBody  CreateCustomerRequest request){
        return ResponseEntity.ok(customerService.updateCustomer(id, request));

    }


}
