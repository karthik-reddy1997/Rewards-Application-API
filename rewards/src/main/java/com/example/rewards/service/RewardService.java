package com.example.rewards.service;

import com.example.rewards.model.RewardResponse;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that handles reward points calculation based on transactions.
 */
@Service
public class RewardService {

    private final TransactionRepository transactionRepository;

    /**
     * Constructs the RewardService with required repository.
     *
     * @param transactionRepository the transaction repository
     */
    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Calculates reward points earned for a given purchase amount.
     *
     * Rules:
     * - 2 points for every dollar spent over $100
     * - 1 point for every dollar spent between $50 and $100
     *
     * @param amount the transaction amount in dollars
     * @return reward points earned
     */
    public int calculatePoints(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }
        int points = 0;
        if (amount > 100) {
            points += 2 * (int)(amount - 100);
            points += 50; // for $50-$100 segment
        } else if (amount > 50) {
            points += (int)(amount - 50);
        }
        return points;
    }

    /**
     * Retrieves reward points for a given customer between startDate and endDate.
     * Groups points by month and calculates total points.
     *
     * @param customerId the ID of the customer
     * @param startDate  the start date (inclusive)
     * @param endDate    the end date (inclusive)
     * @return list with one RewardResponse for the customer or empty list if no transactions
     */
    public List<RewardResponse> getRewardsForCustomerBetweenDates(Long customerId, LocalDate startDate, LocalDate endDate) {
        Assert.notNull(customerId, "Customer ID must not be null");
        Assert.notNull(startDate, "Start date must not be null");
        Assert.notNull(endDate, "End date must not be null");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be after or equal to startDate");
        }

        List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);
        if (transactions.isEmpty()) {
            return Collections.emptyList();
        }

        String customerName = transactions.get(0).getCustomerName();

        // Map of month (e.g. "MAY") to points earned that month
        Map<String, Integer> monthlyPoints = new HashMap<>();
        int totalPoints = 0;

        for (Transaction txn : transactions) {
            int points = calculatePoints(txn.getAmount());
            String month = txn.getTransactionDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();

            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
            totalPoints += points;
        }

        RewardResponse response = new RewardResponse();
        response.setCustomerId(customerId);
        response.setCustomerName(customerName);
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        response.setMonthlyPoints(monthlyPoints);
        response.setTotalPoints(totalPoints);

        return Collections.singletonList(response);
    }
}
