package dev.tise.transaction_service.service.impl;

import dev.tise.transaction_service.model.Account;
import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.model.enums.LoanCategory;
import dev.tise.transaction_service.repository.AccountRepository;
import dev.tise.transaction_service.service.AccountService;
import dev.tise.transaction_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    private static final List<String> accountTypes = List.of("SAVINGS", "CURRENT","FIXED_DEPOSIT");

    @Autowired
    private CustomerService customerService;

    @Override
    public String createNewAccount(String name, double balance, String accountType, Integer age, String state, String email) {

        try {
            if (name.length() < 5) {
                return "Name is not long enough";
            }
            if(name.length()>25){
                return "Sorry, name is too long";
            }
            if (balance < 1000) {
                return "Minimum deposit is 1000.0";
            }

            if(accountType.isBlank()){
               return "Sorry, account type field cannot be empty";
            }
            else if (!accountTypes.contains(accountType.toUpperCase().trim())) {
                return "Please select from the listed\nSAVINGS, CURRENT, FIXED_DEPOSIT";
            }

            Account account = new Account();
            account.setAccountType(accountType.toUpperCase());
            account.setName(name);
            account.setBalance(balance);
            account.setAccountNumber(generateRandomNumber());
            account.setActive(true);
            Customer customer = customerService.createNewCustomer(name, age, state, email);

            account.setCustomer(customer);

            Account savedAccount = accountRepository.save(account);


            return "Created successfully with ID "+savedAccount.getId();

        }

        catch (RuntimeException e){
            System.err.println("error : "+e.getMessage());
            return e.getMessage();
        }
        catch(Exception e){
            return "Please ensure you inputted the correct data in their respected fields.";

        }


    }

    public String freezeAccount(String accountNumber, String reason){
        System.err.println("here -----");
        List<String> validReasons = List.of("Suspected Fraud", "Illegal Activity", "Unpaid Debts", "Liabilities", "Government Action", "Bank Policy Violation", "Security Measures", "Account Holder Request");
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);


        if(account.isEmpty()){
            return "This account number does not exist";
        }
        System.err.println("Fetched account from DB :: "+account.get());
        if(!validReasons.contains(reason)){
            return "This is not a valid reason to freeze this account";
        }

        Account userAccount = account.get();
        boolean activeState = userAccount.isActive();


        if (!activeState){
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

            if (accountRepository.findByAccountNumber(accountNumber).isPresent()){
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
                case 0, 1,2,3,4,5,6,7,8,9,10 -> LoanCategory.TIER_ONE.name();
                case 11,12,13,14,15,16,17,18,19,20  -> LoanCategory.TIER_TWO.name();
                case 21,22,23,24,25, 26, 27, 28, 29, 30 -> LoanCategory.TIER_THREE.name();
                default -> "Invalid balance entered : " + balance;
            };

        } catch (Exception e){
            System.err.println("Error:: " + e.getMessage());
            return("Server currently down...");
        }

    }
}
