package dev.tise.transaction_service.controller;

import dev.tise.transaction_service.request.CreateAccountRequest;
import dev.tise.transaction_service.request.FreezeAccountRequest;
import dev.tise.transaction_service.request.TransferRequest;
import dev.tise.transaction_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")

public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest request){
        System.err.println("Incoming request for account creation :: " + request);
        String output = accountService.createNewAccount(request.getName(), request.getBalance(), request.getAccountType());
        if (output.contains("successfully")){

            return new ResponseEntity<>(output, HttpStatus.CREATED);
        }else {
            return ResponseEntity.badRequest().body(output);
        }
    }
    @PatchMapping("/freeze-account")
    public ResponseEntity<String> freezeAccount(@RequestBody FreezeAccountRequest request) {
        System.err.println("Freezing account... " + request);
        String output = accountService.freezeAccount(request.getAccountNumber(), request.getReason());
        if (output.contains("frozen")) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        }
        else{
            return ResponseEntity.badRequest().body(output);

        }
    }

}
