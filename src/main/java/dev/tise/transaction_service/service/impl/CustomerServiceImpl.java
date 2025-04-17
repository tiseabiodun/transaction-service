package dev.tise.transaction_service.service.impl;

import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.repository.CustomerRepository;
import dev.tise.transaction_service.request.CreateCustomerRequest;
import dev.tise.transaction_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createNewCustomer(String name, LocalDate dob, String state, String email) {
        try {
            if (name == null || name.trim().isEmpty()){
                System.err.println("Error: Customer name is required");
                throw new RuntimeException("Error: Customer name is required");
            }
            if (name.length() > 20){
                System.err.println("Error: Name must not exceed 20 characters");
                throw new RuntimeException("Error: Name must not exceed 20 characters");
            }

            int age = Period.between(dob, LocalDate.now()).getYears();
            Customer customer = new Customer();

            Optional<Customer> optionalCustomer = customerRepository.findByEmailIgnoreCase(email);
            if (optionalCustomer.isPresent()){
                System.out.println("Customer already exists with email  :: "+email);
                return optionalCustomer.get();
            }
            if (age<18){
                throw new RuntimeException("Age is too small, please try again");
            }
            customer.setAge(age);

            Customer newCustomer = new Customer(name, age, state, email);
            return customerRepository.save(newCustomer);

        } catch (Exception e){
            System.err.println("We could not create customer, please try again");
            throw new RuntimeException("We could not create customer, please try again");
        }

    }

    @Override
    public String insertData(MultipartFile file) {
        String line;
        String fileName = file.getOriginalFilename();

        // Check if the file has a valid CSV extension
        if (fileName == null || !fileName.endsWith(".csv")) {
            return "Please upload a csv file";
        }


        System.err.println("Insertion about to begin");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");

                String name = values[0].trim();
                int age = Integer.parseInt(values[1].trim());
                String state = values[2].trim();
                String email = values[3].trim();


                Optional<Customer> existingCustomer = customerRepository.findByEmailIgnoreCase(email);
                if (existingCustomer.isPresent()) {
                    System.out.println("Customer with email " + email + " already exists. Skipping...");
                    continue;
                }

                if(age<1){
                    System.err.println("Age where email is: " + email + "has been skipped because its less than normal");

                    continue;
                }

                if (name.matches(".*\\d.*")){
                    System.err.println("Name where email is: " + email + "has been skipped due to presence of digit");

                    continue;
                }
                if (state.matches(".*\\d.*")){
                    System.err.println("State where email is: " + email + "has been skipped due to presence of digit");
                    continue;
                }

                if (name.trim().isEmpty()){
                    System.err.println("Name where email is: " + email + "has been skipped due to absence");
                    continue;
                }
                if (name.length() > 20){
                    System.err.println("Name where email is: " + email + "has been skipped due to name length");
                    continue;
                }

                Customer customer = new Customer();
                customer.setName(values[0].trim());
                customer.setAge(Integer.parseInt(values[1].trim()));
                customer.setState(values[2].trim().toLowerCase());
                customer.setEmail(values[3].trim().toLowerCase());

                System.err.println("Checking for customer file :: " + customer);

                customerRepository.save(customer);
            }
        } catch (IOException e) {
            System.err.println("Error ::" + e.getMessage());
        }
        catch (NumberFormatException e){
            System.err.println("Error :: " + e.getMessage());
        }


        return "Successfully added";
    }

    @Override
    public String updateCustomer(Long id, CreateCustomerRequest request) {

        System.err.println("I entered here");
        try {
            if (id == null){
                return "Id cannot be null";
            }

            System.err.println("I came out ");


            Optional<Customer> optionalCustomer = customerRepository.findById(id);


            if (optionalCustomer.isPresent()){
                Customer customer = optionalCustomer.get();
                if (request.getName() != null && !request.getName().trim().isEmpty()){
                    if (request.getName().length() > 20){
                        return "Error: Name must not exceed 20 characters";
                    }
                    customer.setName(request.getName());
                }
                Integer age =  request.getAge();

                if (age != null && age < 100 && age > 0) {
                    customer.setAge(age);
                }

                if (request.getState() != null && !request.getState().trim().isEmpty() ){
                    customer.setState(request.getState());
                }
                if (request.getEmail() != null && !request.getEmail().trim().isEmpty()){
                    customer.setEmail(request.getEmail());
                }

                System.err.println("About to update customer");
                customerRepository.save(customer);
                System.err.println("Update success");

            }else {
                return "Customer with id "+ id + " not found";
            }

        }catch (Exception ex){
            System.err.println("An error occurred while updating customer with id "+id+ ": "+ex.getMessage());
            return ex.getMessage();
        }
        return "Customer update successful";
    }

    @Override
    public String deleteCustomer(Long id) {
        return "";
    }

    @Override
    public Customer findCustomerById(Long id) {
        if (id == null){
            throw new RuntimeException("Customer id is required");
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }else{
            throw new  RuntimeException("Failed to get customer with Id : "+id);
        }

    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }
}
