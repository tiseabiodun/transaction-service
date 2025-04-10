package dev.tise.transaction_service.service;

import dev.tise.transaction_service.model.Customer;

public interface AccountService {
    String createNewAccount(String customerName, double balance, String accountType);
    String freezeAccount(String AccountNumber, String reason);
    String getLoanAmount(String balance);
}
