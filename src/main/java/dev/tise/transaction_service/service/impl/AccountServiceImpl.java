package dev.tise.transaction_service.service.impl;

import dev.tise.transaction_service.model.Account;
import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.model.enums.LoanCategory;
import dev.tise.transaction_service.repository.AccountRepository;
import dev.tise.transaction_service.service.AccountService;
import dev.tise.transaction_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    @Value("${otp.generate}")
    private String otpGenerateUrl;

    @Value("${otp.validate}")
    private String otpValidateUrl;

    @Autowired
    private AccountRepository accountRepository;
    private static final List<String> accountTypes = List.of("SAVINGS", "CURRENT", "FIXED_DEPOSIT");

    @Autowired
    private CustomerService customerService;

    @Override
    public String createNewAccount(String name, double balance, String accountType, LocalDate dob, String state, String email) {

        try {
            if (name.length() < 5) {
                return "Name is not long enough";
            }
            if (name.length() > 25) {
                return "Sorry, name is too long";
            }
            if (balance < 1000) {
                return "Minimum deposit is 1000.0";
            }

            if (accountType.isBlank()) {
                return "Sorry, account type field cannot be empty";
            } else if (!accountTypes.contains(accountType.toUpperCase().trim())) {
                return "Please select from the listed\nSAVINGS, CURRENT, FIXED_DEPOSIT";
            }

            Account account = new Account();
            account.setAccountType(accountType.toUpperCase());
            account.setName(name);
            account.setBalance(balance);
            account.setAccountNumber(generateRandomNumber());
            Customer customer = customerService.createNewCustomer(name, dob, state, email);

            account.setCustomer(customer);

            Account savedAccount = accountRepository.save(account);
            System.out.println("Created successfully with ID " + savedAccount.getId());

            String accountNumber = savedAccount.getAccountNumber();

            otpGenerateUrl = otpGenerateUrl.replace("[EMAIL]", email);
            otpGenerateUrl = otpGenerateUrl.replace("[ACCT]", accountNumber);

            RestTemplate restTemplate = new RestTemplate();

            System.err.println("Url to generate OTP :: " + otpGenerateUrl);

            ResponseEntity<String> response = restTemplate.postForEntity(otpGenerateUrl, new HashMap<>(), String.class);

            System.err.println("Response from OTP generation :: " + response);
            if (Objects.nonNull(response.getBody())) {
                return "Account created".concat(": ").concat(response.getBody());
            } else {
                return "Failed to generate OTP for new customer";
            }
            //todo: call generate otp


        } catch (RuntimeException e) {
            System.err.println("error : " + e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            return "Please ensure you inputted the correct data in their respected fields.";

        }


    }

    public String freezeAccount(String accountNumber, String reason) {
        System.err.println("here -----");
        List<String> validReasons = List.of("Suspected Fraud", "Illegal Activity", "Unpaid Debts", "Liabilities", "Government Action", "Bank Policy Violation", "Security Measures", "Account Holder Request");
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);


        if (account.isEmpty()) {
            return "This account number does not exist";
        }
        System.err.println("Fetched account from DB :: " + account.get());
        if (!validReasons.contains(reason)) {
            return "This is not a valid reason to freeze this account";
        }

        Account userAccount = account.get();
        boolean activeState = userAccount.isActive();


        if (!activeState) {
            return "Account has already been frozen";
        }
        System.err.println("Before setting status");

        userAccount.setActive(false);
        accountRepository.save(userAccount);
        System.err.println("After setting status");

        return "Account has been frozen due to: " + reason;
    }


    public String generateRandomNumber() {
        Random random = new Random();
        String accountNumber;

        StringBuilder accountNumberBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumberBuilder.append(random.nextInt(10)); // Generates a digit from 0 to 9
        }
        accountNumber = accountNumberBuilder.toString();

        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            System.err.println("Account exists");
            generateRandomNumber();
        }

        return accountNumber;

    }


    @Override
    public String getLoanAmount(String bal) {

        try {
            double balance = Double.parseDouble(bal);
            balance = balance / 100;
            return switch ((int) balance) {
                case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 -> LoanCategory.TIER_ONE.name();
                case 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 -> LoanCategory.TIER_TWO.name();
                case 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 -> LoanCategory.TIER_THREE.name();
                default -> "Invalid balance entered : " + balance;
            };

        } catch (Exception e) {
            System.err.println("Error:: " + e.getMessage());
            return ("Server currently down...");
        }

    }

    @Override
    public Map<String, String> activateAccount(String otp, String email, String accountNumber) {

        Optional<Customer> optionalCustomer = customerService.findByEmail(email);

        if (email == null || email.trim().isEmpty()) {
            return Map.of("error", "Email is required");
        }
        if (optionalCustomer.isEmpty()) {
            return Map.of("error", "Customer with email " + email + " does not exist");
        }

        Map<String,String> details = new HashMap<>();
        details.put("token", otp);
        details.put("email",email);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(otpValidateUrl,details,String.class );
        System.out.println("Response from validate otp :: "+response);

        if (Objects.equals(response.getBody(), "Token has been validated")){

            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);

            if(optionalAccount.isEmpty()){
                return(Map.of("error", "Account does not exist"));
            }

            Account customerAccount = optionalAccount.get();
            customerAccount.setActive(true);
            accountRepository.save(customerAccount);

            System.err.println("Success in saving the customer account ::" + customerAccount);

            return(Map.of("Success", "Account has been validated"));

        }

        return Map.of();
    }
}
