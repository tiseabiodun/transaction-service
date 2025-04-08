package dev.tise.transaction_service.service.impl;

import dev.tise.transaction_service.model.Account;
import dev.tise.transaction_service.model.Transaction;
import dev.tise.transaction_service.repository.AccountRepository;
import dev.tise.transaction_service.repository.TransactionRepository;
import dev.tise.transaction_service.request.CreateStatementOfAccountRequest;
import dev.tise.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import java.io.FileOutputStream;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public String transfer(String senderAccountNumber, String recipientAccountNumber, double amount) {

        Optional<Account> optionalAccountSender = accountRepository.findByAccountNumber(senderAccountNumber);
        Optional<Account> optionalAccountRecipient = accountRepository.findByAccountNumber(recipientAccountNumber);

        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setAmount(amount);
        transaction.setCreditAccountNumber(senderAccountNumber);
        transaction.setDebitAccountNumber(recipientAccountNumber);

        try {

            if (optionalAccountSender.isEmpty()) {
                return "Sorry, sender's account number doesn't exist";
            }
            if (optionalAccountRecipient.isEmpty()) {
                transaction.setStatus("FAILED");
                transaction.setReason("Recipient's account does not exist");
                transactionRepository.save(transaction);
                return "Sorry, recipient's account number doesn't exist";
            }

            Account senderAccount = optionalAccountSender.get();
            Account recipientAccount = optionalAccountRecipient.get();

            if (senderAccount.getBalance() < amount) {
                transaction.setStatus("FAILED");
                transaction.setReason("Insufficient balance to complete this transaction");
                transactionRepository.save(transaction);
                return "Sorry, you have insufficient balance to complete this transaction";
            }

            senderAccount.setBalance(senderAccount.getBalance() - amount);
            recipientAccount.setBalance(recipientAccount.getBalance() + amount);
            Account transferSender = accountRepository.save(senderAccount);
            Account transferRecipient = accountRepository.save(recipientAccount);

            transaction.setStatus("SUCCESS");
            transaction.setReason("Transaction Completed");
            transactionRepository.save(transaction);

            return "Transfer Complete :: " + transferRecipient.getAccountNumber() + " and " + transferSender.getAccountNumber() + " Updated!";
        } catch (Exception e) {
            System.err.println("Failed to complete transaction :: " + e.getMessage());
            transaction.setStatus("FAILED");
            transaction.setReason(e.getMessage());
            transactionRepository.save(transaction);
            return "Sorry, we could not complete transaction, please try again later";
        }


    }

    @Override
    public List<Transaction> statementOfAccount(CreateStatementOfAccountRequest request) {
        try {
            String userAccountNumber = request.getUserAccountNumber();
            System.err.println("User account number :"+userAccountNumber);
            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(userAccountNumber);
            if (optionalAccount.isEmpty()) {
                System.err.println("Account does not exists");
                return new ArrayList<>();

            }


            String status = request.getStatus();

            List<Transaction> userCreditTransactions;
            List<String> validStatus = List.of("FAILED", "SUCCESS");
            List<Transaction> userDebitTransactions;
            if (Objects.isNull(status) || status.isEmpty()|| !validStatus.contains(status.toUpperCase())){

                userCreditTransactions = transactionRepository.findAllByCreditAccountNumber(userAccountNumber);
                 userDebitTransactions = transactionRepository.findAllByDebitAccountNumber(userAccountNumber);
            }else{
                status = status.toUpperCase();
                userCreditTransactions = transactionRepository.findAllByCreditAccountNumberAndStatus(userAccountNumber,status);
                userDebitTransactions = transactionRepository.findAllByDebitAccountNumberAndStatus(userAccountNumber, status);
            }


            List<Optional<Transaction>> transactions = new ArrayList<>();


            userCreditTransactions.addAll(userDebitTransactions);
            String filePath = String.valueOf(ExcelGenerator.generateStatementOfAccountExcel(userCreditTransactions, userAccountNumber));
            System.out.println("Statement of account saved at: " + filePath);
            return userCreditTransactions;



        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("Server error!");
            return new ArrayList<>();
        }
    }

    public static class ExcelGenerator {

        public static ByteArrayOutputStream generateStatementOfAccountExcel(List<Transaction> transactions, String accountNumber) {
            try (Workbook workbook = new XSSFWorkbook();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                Sheet sheet = workbook.createSheet("Statement of Account");

                Row headerRow = sheet.createRow(0);
                String[] headers = {"Date", "Amount", "Credit Account", "Debit Account", "Status", "Reason"};

                CellStyle headerCellStyle = getHeaderCellStyle(workbook); // Assuming this method exists

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerCellStyle);
                }

                int rowNum = 1;
                for (Transaction transaction : transactions) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(transaction.getDate().toString());
                    row.createCell(1).setCellValue(transaction.getAmount());
                    row.createCell(2).setCellValue(transaction.getCreditAccountNumber());
                    row.createCell(3).setCellValue(transaction.getDebitAccountNumber());
                    row.createCell(4).setCellValue(transaction.getStatus());
                    row.createCell(5).setCellValue(transaction.getReason());
                }

                // Auto-size columns
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write workbook content to the output stream
                workbook.write(outputStream);
                return outputStream;

            } catch (IOException e) {
                e.printStackTrace();
                return null; // Handle errors properly in calling methods
            }
        }
        private static CellStyle getHeaderCellStyle(Workbook workbook) {
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            return headerStyle;
        }
    }
}
