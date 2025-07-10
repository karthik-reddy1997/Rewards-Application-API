package com.example.rewards.service;

import com.example.rewards.model.RewardResponse;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for RewardService.
 */
@SpringBootTest
class RewardServiceTests {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();

        // Insert sample transactions
        transactionRepository.save(new Transaction(null, 1L, "John Doe", 120.0, LocalDate.of(2025, 5, 15)));
        transactionRepository.save(new Transaction(null, 1L, "John Doe", 75.0, LocalDate.of(2025, 5, 20)));
        transactionRepository.save(new Transaction(null, 1L, "John Doe", 45.0, LocalDate.of(2025, 6, 5)));
        transactionRepository.save(new Transaction(null, 2L, "Jane Smith", 130.0, LocalDate.of(2025, 6, 7)));
        transactionRepository.save(new Transaction(null, 2L, "Jane Smith", 85.0, LocalDate.of(2025, 7, 3)));
        transactionRepository.save(new Transaction(null, 3L, "Alice Johnson", 150.0, LocalDate.of(2025, 7, 10)));
    }

    @Test
    @DisplayName("Calculate points correctly for various amounts")
    void calculatePoints_shouldReturnCorrectPoints() {
        assertEquals(0, rewardService.calculatePoints(30));
        assertEquals(25, rewardService.calculatePoints(75));  // 75-50=25 points
        assertEquals(90, rewardService.calculatePoints(120)); // 2*(20)+50=90
        assertEquals(150, rewardService.calculatePoints(150)); // 2*50+50=150 points

        // Let's double check calculation for 150:
        // Points = 2 * (150 - 100) + 50 = 2*50 + 50 = 100 + 50 = 150 points
        assertEquals(150, rewardService.calculatePoints(150));
    }

    @Test
    @DisplayName("Get rewards for customer 1 within May 2025")
    void getRewardsForCustomerBetweenDates_shouldReturnCorrectPoints() {
        Long customerId = 1L;
        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 31);

        List<RewardResponse> rewards = rewardService.getRewardsForCustomerBetweenDates(customerId, start, end);

        assertNotNull(rewards);
        assertEquals(1, rewards.size());

        RewardResponse response = rewards.get(0);
        assertEquals(customerId, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals(start, response.getStartDate());
        assertEquals(end, response.getEndDate());

        // Monthly points: May = 120 + 75 transactions
        // Points for $120 = 90
        // Points for $75 = 25
        assertTrue(response.getMonthlyPoints().containsKey("MAY"));
        assertEquals(115, response.getMonthlyPoints().get("MAY"));
        assertEquals(115, response.getTotalPoints());
    }

    @Test
    @DisplayName("Returns empty list if no transactions found")
    void getRewardsForCustomerBetweenDates_noTransactions() {
        Long customerId = 99L;
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        List<RewardResponse> rewards = rewardService.getRewardsForCustomerBetweenDates(customerId, start, end);

        assertNotNull(rewards);
        assertTrue(rewards.isEmpty());
    }

    @Test
    @DisplayName("Throws IllegalArgumentException for invalid dates")
    void getRewardsForCustomerBetweenDates_invalidDates() {
        Long customerId = 1L;
        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 5, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rewardService.getRewardsForCustomerBetweenDates(customerId, start, end));

        assertEquals("endDate must be after or equal to startDate", exception.getMessage());
    }

    @Test
    @DisplayName("Throws IllegalArgumentException for negative amount in calculatePoints")
    void calculatePoints_negativeAmount_throws() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rewardService.calculatePoints(-10));

        assertEquals("Transaction amount cannot be negative", exception.getMessage());
    }
}
