package com.finance.sdp.service;

import java.util.List;
import java.util.Map;

import com.finance.sdp.dto.MonthlyTrendPoint;
import com.finance.sdp.model.Transaction;

public interface TransactionService {
	String addIncome(Transaction transaction);
	String addExpense(Transaction transaction);
	List<Transaction> getTransactionsByDate(String date);
	List<Transaction> getAllIncomes();
	List<Transaction> getAllExpenses();
	Double getTotalIncome();
	Double getTotalExpense();
	Double getNetBalance();
	List<Transaction> getAllTransactions();
	List<Transaction> getTransactionsByType(String type);
	List<Transaction> getTransactionsByCategory(String category);
	List<Transaction> getRecentTransactions(int limit);
	List<Transaction> getTransactionsByDateRange(String startDate, String endDate);
	Double getAverageIncome();
	Double getAverageExpense();
	Map<String, Double> getCategoryTotals();
	List<MonthlyTrendPoint> getMonthlyTrends(int months);
	String updateTransaction(String transactionId, Transaction updatedTransaction);
	String deleteTransaction(String transactionId);
}
