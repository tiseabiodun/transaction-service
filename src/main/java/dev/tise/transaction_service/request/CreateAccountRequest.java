package dev.tise.transaction_service.request;

import java.time.LocalDate;

public class CreateAccountRequest {
    private String name;
    private double balance;
    private String accountType;
    private LocalDate dob;
    private String state;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                ", dob=" + dob +
                ", state='" + state + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
