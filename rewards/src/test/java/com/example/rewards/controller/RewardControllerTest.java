package com.example.rewards.controller;

import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();

        transactionRepository.save(new Transaction(null, 1L, "John Doe", 120.0, LocalDate.of(2025, 5, 15)));
        transactionRepository.save(new Transaction(null, 1L, "John Doe", 75.0, LocalDate.of(2025, 5, 20)));
        transactionRepository.save(new Transaction(null, 1L, "John Doe", 45.0, LocalDate.of(2025, 6, 1)));

        transactionRepository.save(new Transaction(null, 2L, "Jane Smith", 130.0, LocalDate.of(2025, 6, 10)));
    }

    @Test
    @DisplayName("Returns correct rewards with real service and DB")
    void getRewards_withRealData_returnsRewards() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("customerId", "1")
                        .param("startDate", "2025-05-01")
                        .param("endDate", "2025-05-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[0].customerName", is("John Doe")))
                .andExpect(jsonPath("$[0].monthlyPoints.MAY", is(115)))
                .andExpect(jsonPath("$[0].totalPoints", is(115)));
    }

    @Test
    @DisplayName("Returns 204 when no rewards found for date range")
    void getRewards_noData_returns204() throws Exception {
        mockMvc.perform(get("/api/rewards")
                        .param("customerId", "2")
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 500 when parameters are missing")
    void getRewards_missingParams_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().is5xxServerError());
    }
}
