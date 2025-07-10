package com.example.rewards.controller;

import com.example.rewards.model.RewardResponse;
import com.example.rewards.service.RewardService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller that exposes rewards endpoints.
 */
@RestController
@RequestMapping("/api/rewards")
@Validated
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Endpoint to get reward points for a customer within a date range.
     *
     * @param customerId customer ID (must be positive)
     * @param startDate  start date in yyyy-MM-dd format (must be past or present)
     * @param endDate    end date in yyyy-MM-dd format (must be past or present)
     * @return list containing reward summary
     */
    @GetMapping
    public ResponseEntity<List<RewardResponse>> getRewards(
            @RequestParam @NotNull @Positive Long customerId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate startDate,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate endDate) {

        List<RewardResponse> rewards = rewardService.getRewardsForCustomerBetweenDates(customerId, startDate, endDate);
        if (rewards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rewards);
    }
}
