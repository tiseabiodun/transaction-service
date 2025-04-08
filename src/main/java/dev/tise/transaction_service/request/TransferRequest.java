package dev.tise.transaction_service.request;

public class TransferRequest {
    private String accountNumberSender;
    private String accountNumberRecipient;
    private double amount;

    public String getAccountNumberSender() {
        return accountNumberSender;
    }

    public void setAccountNumberSender(String accountNumberSender) {
        this.accountNumberSender = accountNumberSender;
    }

    public String getAccountNumberRecipient() {
        return accountNumberRecipient;
    }

    public void setAccountNumberRecipient(String accountNumberRecipient) {
        this.accountNumberRecipient = accountNumberRecipient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "accountNumberSender='" + accountNumberSender + '\'' +
                ", accountNumberRecipient='" + accountNumberRecipient + '\'' +
                ", amount=" + amount +
                '}';
    }
}
