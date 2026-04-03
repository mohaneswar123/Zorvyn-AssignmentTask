package com.finance.sdp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.finance.sdp.enums.Category;
import com.finance.sdp.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String>{

	List<Transaction> findAllByTimestamp(LocalDate timestamp);

	List<Transaction> findAllByTimestampBetween(LocalDate startDate, LocalDate endDate);

	List<Transaction> findAllByTypeIgnoreCase(String type);

	List<Transaction> findAllByCategory(Category category);

}
