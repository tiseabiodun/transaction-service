package dev.tise.transaction_service.service;


import dev.tise.transaction_service.model.Transaction;
import dev.tise.transaction_service.request.CreateStatementOfAccountRequest;


import java.util.List;
import java.util.Optional;

public interface TransactionService {
    String transfer(String senderAccountNumber, String recipientAccountNumber, double amount);
    List<Transaction> statementOfAccount(CreateStatementOfAccountRequest request);
}
