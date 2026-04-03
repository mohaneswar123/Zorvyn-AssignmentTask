package com.finance.sdp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finance.sdp.dto.DashboardSummaryResponse;
import com.finance.sdp.dto.TransactionInsightsResponse;
import com.finance.sdp.model.Transaction;
import com.finance.sdp.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private static final int DEFAULT_DASHBOARD_LIMIT = 5;
    private static final int DEFAULT_MONTH_TREND_WINDOW = 6;

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardData() {
        DashboardSummaryResponse response = new DashboardSummaryResponse(
                transactionService.getTotalIncome(),
                transactionService.getTotalExpense(),
                transactionService.getNetBalance(),
                transactionService.getCategoryTotals(),
                transactionService.getMonthlyTrends(DEFAULT_MONTH_TREND_WINDOW),
                transactionService.getRecentTransactions(DEFAULT_DASHBOARD_LIMIT));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/records")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<Transaction>> getRecords() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/records/date/{date}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<Transaction>> getRecordsByDate(@PathVariable String date) {
        return ResponseEntity.ok(transactionService.getTransactionsByDate(date));
    }

    @GetMapping("/records/range")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<Transaction>> getRecordsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(startDate, endDate));
    }

    @GetMapping("/records/type/{type}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<Transaction>> getRecordsByType(@PathVariable String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    @GetMapping("/records/category/{category}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<Transaction>> getRecordsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(transactionService.getTransactionsByCategory(category));
    }

    @GetMapping("/insights")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<TransactionInsightsResponse> getInsights() {
        TransactionInsightsResponse response = new TransactionInsightsResponse(
                transactionService.getTotalIncome(),
                transactionService.getTotalExpense(),
                transactionService.getNetBalance(),
                transactionService.getAverageIncome(),
                transactionService.getAverageExpense());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/income")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addIncome(@RequestBody Transaction transaction) {
        String message = transactionService.addIncome(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/expense")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addExpense(@RequestBody Transaction transaction) {
        String message = transactionService.addExpense(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTransaction(
            @PathVariable String transactionId,
            @RequestBody Transaction updatedTransaction) {

        return ResponseEntity.ok(transactionService.updateTransaction(transactionId, updatedTransaction));
    }

    @DeleteMapping("/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTransaction(@PathVariable String transactionId) {
        return ResponseEntity.ok(transactionService.deleteTransaction(transactionId));
    }
}
