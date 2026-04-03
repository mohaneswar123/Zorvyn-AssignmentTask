package com.finance.sdp.dto;

public class TransactionInsightsResponse {

    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
    private Double averageIncome;
    private Double averageExpense;

    public TransactionInsightsResponse(Double totalIncome, Double totalExpense, Double netBalance,
            Double averageIncome, Double averageExpense) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
        this.averageIncome = averageIncome;
        this.averageExpense = averageExpense;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public Double getNetBalance() {
        return netBalance;
    }

    public Double getAverageIncome() {
        return averageIncome;
    }

    public Double getAverageExpense() {
        return averageExpense;
    }
}
