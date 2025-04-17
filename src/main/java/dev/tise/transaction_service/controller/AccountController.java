package dev.tise.transaction_service.controller;

import dev.tise.transaction_service.request.CreateAccountRequest;
import dev.tise.transaction_service.request.FreezeAccountRequest;
import dev.tise.transaction_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")

public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest request){
        System.err.println("Incoming request for account creation :: " + request);
        String output = accountService.createNewAccount(request.getName(), request.getBalance(), request.getAccountType(), request.getDob(), request.getState(), request.getEmail());
        System.out.println("Output from create account "+output);
        if (output.contains("successfully") || output.contains("created")){
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

    @GetMapping("/customer-loan-category")
    public ResponseEntity<String> getLoanCategory(@RequestParam  String balance){
        String output = accountService.getLoanAmount(balance);
        System.err.println(output);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/activate-account")
    private ResponseEntity<Map<String, String>> activateAccount(@RequestParam String otp,@RequestParam String email,
            @RequestParam String accountNumber){
        return ResponseEntity.ok(accountService.activateAccount(otp, email, accountNumber));
    }

}
