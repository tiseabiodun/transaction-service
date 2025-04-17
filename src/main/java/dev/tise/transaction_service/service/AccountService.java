package dev.tise.transaction_service.service;

import java.time.LocalDate;
import java.util.Map;

public interface AccountService {
    String createNewAccount(String customerName, double balance, String accountType, LocalDate dob, String state, String email);
    String freezeAccount(String AccountNumber, String reason);
    String getLoanAmount(String balance);

    Map<String, String> activateAccount(String otp, String email, String accountNumber);
}
