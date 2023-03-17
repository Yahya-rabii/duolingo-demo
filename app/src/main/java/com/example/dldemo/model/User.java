package com.example.dldemo.model;


import android.util.Patterns;

import java.util.UUID;

public class User {
	private String fullName, username, email;
	private Boolean isEmailVerified;
	private String im;
	public User() {
	}

	public void setProfileImageUrl(String im) {
		this.im = im;
	}

	public User(String fullName, String email, Boolean isEmailVerified) {
		this.fullName = fullName;
		this.email = email;
		this.isEmailVerified = isEmailVerified;
		generateUsername();
	}
	
	public User(String fullName, String username, String email,Boolean isEmailVerified) {
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.isEmailVerified = isEmailVerified;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	
	public void generateUsername() {
		String newEmail = email.substring(0, email.indexOf("@")) + UUID.randomUUID().toString().substring(0, 5);
		
		if (newEmail.length() > 25) {
			this.username = newEmail.substring(0, 25);
		} else {
			this.username = newEmail;
		}
	}
	
	public String validateName(String name) {
		if (name.trim().length() == 0) {
			return "Name cannot be empty";
		} else if (name.trim().matches("^[0-9]+$")) {
			return "Name cannot have numbers in it";
		} else if (!name.trim().matches("^[a-zA-Z][a-zA-Z ]++$")) {
			return "Invalid Name";
		}
		return null;
	}
	
	public String validateUsername(String username) {
		if (username.trim().length() == 0) {
			return "Username cannot be empty";
		} else if (username.trim().length() < 5) {
			return "Username too small. Minimum length is 5";
		} else if (username.trim().length() > 25) {
			return "Username too long. Maximum length is 25";
		} else if (!username.trim().matches("^[a-zA-Z][a-zA-Z0-9]+$")) {
			return "Username can only contain letters and numbers";
		}
		return null;
	}
	
	public String validateEmail(String email) {
		if (email.trim().length() == 0) {
			return "Email cannot be empty";
		} else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
			return "Invalid Email";
		}
		return null;
	}
	
	public String validatePassword(String password, String confirmPassword) {
		if (password.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (confirmPassword.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (!confirmPassword.trim().matches(password.trim())) {
			return "Passwords do not match";
		} else if (password.trim().length() < 8) {
			return "Password too small. Minimum length is 8";
		} else if (password.trim().length() > 15) {
			return "Password too long. Maximum length is 15";
		} else if (password.trim().contains(" ")) {
			return "Password cannot contain spaces";
		} else if (!(password.trim().contains("@") || password.trim().contains("#")) || password.trim().contains("$") || password.trim().contains("%") || password.trim().contains("*") || password.trim().contains(".") || password.trim().matches("(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))")) {
			return "Password should contain atleast one uppercase character or one number or any of the special characters from (@, #, $, %, *, .)";
		}
		return null;
	}
	
	public String validateConfirmPassword(String confirmPassword, String password) {
		if (confirmPassword.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (password.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (!confirmPassword.trim().matches(password.trim())) {
			return "Passwords do not match";
		} else if (password.trim().length() < 8) {
			return "Password too small. Minimum length is 8";
		} else if (password.trim().length() > 15) {
			return "Password too long. Maximum length is 15";
		} else if (password.trim().contains(" ")) {
			return "Password cannot contain spaces";
		} else if (!(password.trim().contains("@") || password.trim().contains("#")) || password.trim().contains("$") || password.trim().contains("%") || password.trim().contains("*") || password.trim().contains(".") || password.trim().matches("(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))")) {
			return "Password should contain atleast one uppercase character or one number or any of the special characters from (@, #, $, %, *, .)";
		}
		return null;
	}
}