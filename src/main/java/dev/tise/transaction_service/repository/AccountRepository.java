package dev.tise.transaction_service.repository;

import dev.tise.transaction_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Boolean> findActiveByAccountNumber(String accountNumber);

}
