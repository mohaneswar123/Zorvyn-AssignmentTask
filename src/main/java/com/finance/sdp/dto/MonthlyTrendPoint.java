package com.finance.sdp.dto;

public class MonthlyTrendPoint {

    private String month;
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;

    public MonthlyTrendPoint(String month, Double totalIncome, Double totalExpense, Double netBalance) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
    }

    public String getMonth() {
        return month;
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
}
