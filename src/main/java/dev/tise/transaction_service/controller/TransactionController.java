package dev.tise.transaction_service.controller;


import dev.tise.transaction_service.model.Transaction;
import dev.tise.transaction_service.request.CreateStatementOfAccountRequest;
import dev.tise.transaction_service.request.TransferRequest;
import dev.tise.transaction_service.service.TransactionService;
import dev.tise.transaction_service.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/account")

public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);


    @Autowired
    private TransactionService transactionService;

    @GetMapping("/create-soa")
    public ResponseEntity<List<Transaction>> createStatementOfAccount(@RequestBody CreateStatementOfAccountRequest request) {
        System.err.println("Incoming request for statement of account creation :: " + request);
        List<Transaction> output = transactionService.statementOfAccount(request);
        if (output.isEmpty()){
            return new ResponseEntity<>(output, NOT_FOUND);
        }
        return new ResponseEntity<>(output, HttpStatus.CREATED);

    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        System.err.println("Incoming request for transfer :: " + request);
        String output = transactionService.transfer(request.getAccountNumberSender(), request.getAccountNumberRecipient(), request.getAmount());
        if (output.contains("Complete")) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(output);
        }
    }


    @PostMapping("/soa-excel")
    public ResponseEntity<byte[]> downloadStatementAsExcel(@RequestBody CreateStatementOfAccountRequest request) {
        logger.info("Generating Statement of Account Excel file for account: {}", request.getUserAccountNumber());

        List<Transaction> transactions = transactionService.statementOfAccount(request);

        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            // Ensure this method returns ByteArrayOutputStream or byte[]
            ByteArrayOutputStream excelStream = TransactionServiceImpl.ExcelGenerator.generateStatementOfAccountExcel(transactions, request.getUserAccountNumber());

            assert excelStream != null;
            byte[] excelBytes = excelStream.toByteArray();  // Convert to byte array

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "statement_" + request.getUserAccountNumber() + ".xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            logger.error("Failed to generate Excel file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
