package com.finance.sdp.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.finance.sdp.enums.Category;

@Document(collection = "transactions")
public class Transaction {
	
	@Id
	private String id;
	private double amount;
	private String type; // "income" or "expense"
	private Category category;
	private LocalDate timestamp;
	private String description;
	private String createdBy; // User ID 
	
	public Transaction() {
	}
	
	public Transaction(double amount, String type, Category category, LocalDate timestamp, String description, String createdBy) {
		this.amount = amount;
		this.type = type;
		this.category = category;
		this.timestamp = timestamp;
		this.description = description;
		this.createdBy = createdBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
