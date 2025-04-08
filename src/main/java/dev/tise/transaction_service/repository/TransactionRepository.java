package dev.tise.transaction_service.repository;

import dev.tise.transaction_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByCreditAccountNumber(String creditAccount);
    List<Transaction> findAllByDebitAccountNumber(String creditAccount);

    List<Transaction> findAllByCreditAccountNumberAndStatus(String accountNumber,String status);
    List<Transaction> findAllByDebitAccountNumberAndStatus(String accountNumber,String status);


}
