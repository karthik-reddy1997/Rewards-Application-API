package com.example.rewards.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Response DTO summarizing reward points for a customer.
 */

@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class RewardResponse {

    private Long customerId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<String, Integer> monthlyPoints;
    private int totalPoints;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Map<String, Integer> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public RewardResponse(Long customerId, String customerName, LocalDate startDate, LocalDate endDate, Map<String, Integer> monthlyPoints, int totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }
    public RewardResponse() {
        // Default constructor
    }

}
