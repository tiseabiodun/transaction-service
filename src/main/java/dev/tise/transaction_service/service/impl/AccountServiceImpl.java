package dev.tise.transaction_service.service.impl;

import dev.tise.transaction_service.model.Account;
import dev.tise.transaction_service.model.Customer;
import dev.tise.transaction_service.repository.AccountRepository;
import dev.tise.transaction_service.service.AccountService;
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

    @Override
    public String createNewAccount(String name, double balance, String accountType) {

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

        }
        catch (RuntimeException e){
            return "Sorry, the server is currently down, please try again later.";

        }
        catch(Exception e){
            return "Please ensure you inputted the correct data in their respected fields.";

        }


        Account account = new Account();
        account.setAccountType(accountType.toUpperCase());
        account.setName(name);
        account.setBalance(balance);
        account.setAccountNumber(generateRandomNumber());

        Account savedAccount = accountRepository.save(account);

        return "Created successfully with ID "+savedAccount.getId();
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
}
