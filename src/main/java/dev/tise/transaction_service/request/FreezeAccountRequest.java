package dev.tise.transaction_service.request;

public class FreezeAccountRequest {
    private String accountNumber;
    private String reason;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }



    @Override
    public String toString() {
        return "FreezeAccountRequest{" +
                "accountNumber='" + accountNumber + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
