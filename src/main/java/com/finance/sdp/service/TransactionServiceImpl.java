package com.finance.sdp.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.finance.sdp.dto.MonthlyTrendPoint;
import com.finance.sdp.enums.Category;
import com.finance.sdp.model.Transaction;
import com.finance.sdp.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final String INCOME = "income";
	private static final String EXPENSE = "expense";

	private final TransactionRepository transactionRepository;

	public TransactionServiceImpl(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Override
	public String addIncome(Transaction transaction) {
		Transaction validatedTransaction = prepareTransactionForCreate(transaction, INCOME);
		transactionRepository.save(validatedTransaction);
		return "Income added successfully";
	}

	@Override
	public String addExpense(Transaction transaction) {
		Transaction validatedTransaction = prepareTransactionForCreate(transaction, EXPENSE);
		transactionRepository.save(validatedTransaction);
		return "Expense added successfully";
	}

	@Override
	public List<Transaction> getTransactionsByDate(String date) {
		LocalDate parsedDate = parseDate(date, "date");
		return transactionRepository.findAllByTimestamp(parsedDate);
	}

	@Override
	public List<Transaction> getAllIncomes() {
		return transactionRepository.findAllByTypeIgnoreCase(INCOME);
	}

	@Override
	public List<Transaction> getAllExpenses() {
		return transactionRepository.findAllByTypeIgnoreCase(EXPENSE);
	}

	@Override
	public Double getTotalIncome() {
		return getAllIncomes().stream()
				.mapToDouble(Transaction::getAmount)
				.sum();
	}

	@Override
	public Double getTotalExpense() {
		return getAllExpenses().stream()
				.mapToDouble(Transaction::getAmount)
				.sum();
	}

	@Override
	public Double getNetBalance() {
		return getTotalIncome() - getTotalExpense();
	}

	@Override
	public List<Transaction> getAllTransactions() {
		return transactionRepository.findAll();
	}

	@Override
	public List<Transaction> getTransactionsByType(String type) {
		String normalizedType = normalizeType(type);
		return transactionRepository.findAllByTypeIgnoreCase(normalizedType);
	}

	@Override
	public List<Transaction> getTransactionsByCategory(String category) {
		Category parsedCategory = parseCategory(category);
		return transactionRepository.findAllByCategory(parsedCategory);
	}

	@Override
	public List<Transaction> getRecentTransactions(int limit) {
		if (limit <= 0) {
			throw new IllegalArgumentException("Limit must be greater than 0");
		}

		return getAllTransactions().stream()
				.sorted(Comparator.comparing(Transaction::getTimestamp, Comparator.nullsLast(LocalDate::compareTo)).reversed())
				.limit(limit)
				.toList();
	}

	@Override
	public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
		LocalDate fromDate = parseDate(startDate, "startDate");
		LocalDate toDate = parseDate(endDate, "endDate");

		if (fromDate.isAfter(toDate)) {
			throw new IllegalArgumentException("startDate cannot be after endDate");
		}

		return transactionRepository.findAllByTimestampBetween(fromDate, toDate).stream()
				.sorted(Comparator.comparing(Transaction::getTimestamp))
				.toList();
	}

	@Override
	public Double getAverageIncome() {
		List<Transaction> incomes = getAllIncomes();
		if (incomes.isEmpty()) {
			return 0.0;
		}

		return incomes.stream()
				.mapToDouble(Transaction::getAmount)
				.average()
				.orElse(0.0);
	}

	@Override
	public Double getAverageExpense() {
		List<Transaction> expenses = getAllExpenses();
		if (expenses.isEmpty()) {
			return 0.0;
		}

		return expenses.stream()
				.mapToDouble(Transaction::getAmount)
				.average()
				.orElse(0.0);
	}

	@Override
	public Map<String, Double> getCategoryTotals() {
		Map<String, Double> totals = new LinkedHashMap<>();

		for (Transaction transaction : getAllTransactions()) {
			if (transaction.getCategory() == null) {
				continue;
			}

			String key = transaction.getCategory().name();
			totals.put(key, totals.getOrDefault(key, 0.0) + transaction.getAmount());
		}

		return totals;
	}

	@Override
	public List<MonthlyTrendPoint> getMonthlyTrends(int months) {
		if (months <= 0) {
			throw new IllegalArgumentException("Months must be greater than 0");
		}

		YearMonth currentMonth = YearMonth.now();
		List<MonthlyTrendPoint> trends = new ArrayList<>();
		List<Transaction> allTransactions = getAllTransactions();

		for (int index = months - 1; index >= 0; index--) {
			YearMonth targetMonth = currentMonth.minusMonths(index);
			double income = 0.0;
			double expense = 0.0;

			for (Transaction transaction : allTransactions) {
				if (transaction.getTimestamp() == null) {
					continue;
				}

				YearMonth transactionMonth = YearMonth.from(transaction.getTimestamp());
				if (!transactionMonth.equals(targetMonth)) {
					continue;
				}

				String normalizedType = normalizeStoredType(transaction.getType());
				if (INCOME.equals(normalizedType)) {
					income += transaction.getAmount();
				} else if (EXPENSE.equals(normalizedType)) {
					expense += transaction.getAmount();
				}
			}

			trends.add(new MonthlyTrendPoint(
					targetMonth.toString(),
					income,
					expense,
					income - expense));
		}

		return trends;
	}

	@Override
	public String updateTransaction(String transactionId, Transaction updatedTransaction) {
		if (updatedTransaction == null) {
			throw new IllegalArgumentException("Updated transaction is required");
		}

		Transaction existingTransaction = getTransactionById(transactionId);

		if (updatedTransaction.getAmount() > 0) {
			existingTransaction.setAmount(updatedTransaction.getAmount());
		}

		if (updatedTransaction.getType() != null && !updatedTransaction.getType().isBlank()) {
			existingTransaction.setType(normalizeType(updatedTransaction.getType()));
		}

		if (updatedTransaction.getCategory() != null) {
			existingTransaction.setCategory(updatedTransaction.getCategory());
		}

		if (updatedTransaction.getTimestamp() != null) {
			existingTransaction.setTimestamp(updatedTransaction.getTimestamp());
		}

		if (updatedTransaction.getDescription() != null) {
			existingTransaction.setDescription(updatedTransaction.getDescription().trim());
		}

		transactionRepository.save(existingTransaction);
		return "Transaction updated successfully";
	}

	@Override
	public String deleteTransaction(String transactionId) {
		Transaction transaction = getTransactionById(transactionId);
		transactionRepository.delete(transaction);
		return "Transaction deleted successfully";
	}

	private Transaction prepareTransactionForCreate(Transaction transaction, String type) {
		if (transaction == null) {
			throw new IllegalArgumentException("Transaction payload is required");
		}
		if (transaction.getAmount() <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
		if (transaction.getCategory() == null) {
			throw new IllegalArgumentException("Category is required");
		}

		transaction.setType(type);
		if (transaction.getTimestamp() == null) {
			transaction.setTimestamp(LocalDate.now());
		}
		if (transaction.getDescription() != null) {
			transaction.setDescription(transaction.getDescription().trim());
		}

		return transaction;
	}

	private LocalDate parseDate(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " is required");
		}

		try {
			return LocalDate.parse(value.trim());
		} catch (DateTimeParseException exception) {
			throw new IllegalArgumentException(fieldName + " must be in yyyy-MM-dd format", exception);
		}
	}

	private Category parseCategory(String category) {
		if (category == null || category.isBlank()) {
			throw new IllegalArgumentException("Category is required");
		}

		for (Category value : Category.values()) {
			if (value.name().equalsIgnoreCase(category.trim())) {
				return value;
			}
		}

		throw new IllegalArgumentException("Invalid category provided");
	}

	private String normalizeType(String type) {
		if (type == null || type.isBlank()) {
			throw new IllegalArgumentException("Type is required");
		}

		String normalizedType = type.trim().toLowerCase(Locale.ROOT);
		if (!INCOME.equals(normalizedType) && !EXPENSE.equals(normalizedType)) {
			throw new IllegalArgumentException("Type must be either income or expense");
		}

		return normalizedType;
	}

	private String normalizeStoredType(String type) {
		if (type == null) {
			return "";
		}
		return type.trim().toLowerCase(Locale.ROOT);
	}

	private Transaction getTransactionById(String transactionId) {
		if (transactionId == null || transactionId.isBlank()) {
			throw new IllegalArgumentException("transactionId is required");
		}

		return transactionRepository.findById(transactionId.trim())
				.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
	}
}
