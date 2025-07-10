package com.example.rewards.repository;

import com.example.rewards.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for accessing Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for a specific customer occurring between the given start and end dates (inclusive).
     *
     * @param customerId the ID of the customer
     * @param startDate  the start date (inclusive)
     * @param endDate    the end date (inclusive)
     * @return list of transactions matching criteria
     */
    List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
