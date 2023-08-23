package com.example.project1.bean;

public class OrderRequest {
	private Long userId;

	private Double amount;
	public Long getUserId() {
		return userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
