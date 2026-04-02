package com.finance.sdp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.finance.sdp.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String>{

	List<Transaction> findAllByUserIdAndDate(String userId, String date);

	List<Transaction> findAllByUserId(String userId);

}
