package com.example.project1.entity;

import jakarta.persistence.*;
@Entity
public class User extends AbstractEntity {

	@Column(name = "user_name")
	private String userName;
	@Column(name = "email")
	private String email;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
