package dev.tise.transaction_service.service;

import dev.tise.transaction_service.model.Customer;

import java.util.Optional;

public interface AccountService {
    String createNewAccount(String customerName, double balance, String accountType, Integer age, String state, String email);
    String freezeAccount(String AccountNumber, String reason);
    String getLoanAmount(String balance);
}
