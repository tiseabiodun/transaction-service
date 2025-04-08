package dev.tise.transaction_service.request;


import java.util.Date;

public class CreateStatementOfAccountRequest {
    private String userAccountNumber;
    private String status;
    private Date date;

    public String getUserAccountNumber() {
        return userAccountNumber;
    }

    public void setUserAccountNumber(String userAccountNumber) {
        this.userAccountNumber = userAccountNumber;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CreateStatementOfAccountRequest{" +
                "senderAccountNumber='" + userAccountNumber + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
