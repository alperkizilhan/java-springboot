package com.example.project1.entity;

import jakarta.persistence.*;
@Entity
public class Order extends AbstractEntity {

	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	@Column(name = "user_id", nullable = false)
	private Long userId;


	@Column(name="amount",nullable = true)
	private Double amount;

	public enum Status {
		CREATED, IN_DELIVERY, DELIVERED, CANCELLED;
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}




	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
