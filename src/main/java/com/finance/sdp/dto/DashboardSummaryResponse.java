package com.finance.sdp.dto;

import java.util.List;
import java.util.Map;

import com.finance.sdp.model.Transaction;

public class DashboardSummaryResponse {

    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
    private Map<String, Double> categoryTotals;
    private List<MonthlyTrendPoint> monthlyTrends;
    private List<Transaction> recentTransactions;

    public DashboardSummaryResponse(Double totalIncome, Double totalExpense, Double netBalance,
            Map<String, Double> categoryTotals,
            List<MonthlyTrendPoint> monthlyTrends,
            List<Transaction> recentTransactions) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
        this.categoryTotals = categoryTotals;
        this.monthlyTrends = monthlyTrends;
        this.recentTransactions = recentTransactions;
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

    public Map<String, Double> getCategoryTotals() {
        return categoryTotals;
    }

    public List<MonthlyTrendPoint> getMonthlyTrends() {
        return monthlyTrends;
    }

    public List<Transaction> getRecentTransactions() {
        return recentTransactions;
    }
}
