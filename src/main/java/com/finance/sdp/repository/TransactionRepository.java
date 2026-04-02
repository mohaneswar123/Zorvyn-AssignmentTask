package com.finance.sdp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.finance.sdp.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String>{

	List<Transaction> findAllByCreatedByAndTimestamp(String createdBy, LocalDate timestamp);

	List<Transaction> findAllByCreatedBy(String createdBy);

}
