package dev.tise.transaction_service.model;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "transaction")



public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String creditAccountNumber;
    private String debitAccountNumber;
    private String status;
    private double amount;
    private String reason;
    private Date date;


    public Transaction() {
    }

    public String getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public void setCreditAccountNumber(String creditAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public void setDebitAccountNumber(String debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderAccountNumber='" + creditAccountNumber + '\'' +
                ", recipientAccountNumber='" + debitAccountNumber + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", reason='" + reason + '\'' +
                ", date=" + date +
                '}';
    }
}


