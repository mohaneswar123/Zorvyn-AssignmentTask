package com.finance.sdp.service;

import java.util.List;

import com.finance.sdp.model.Transaction;

public interface TransactionService {
	String addIncome(Transaction transaction);
	String addExpense(Transaction transaction);
	List<Transaction> getTransactionsByDate(String userId, String date);
	List<Transaction> getAllIncomes(String userId);
	List<Transaction> getAllExpenses(String userId);
	Double getTotalIncome(String userId);
	Double getTotalExpense(String userId);
	Double getNetBalance(String userId);
	List<Transaction> getAllTransactions(String userId);
	List<Transaction> getTransactionsByType(String userId, String type);
	List<Transaction> getTransactionsByCategory(String userId, String category);
}
