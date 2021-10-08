package com.ryan.authentication.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginUser {
	@NotEmpty(message = "You must have an email.")
	@Email(message = "Email must be valid.")
    private String email;
    @NotEmpty(message = "Must create a password")
	@Size(min=6, max=128, message = "Password must be at least 6 characters.")
    private String password;
    
    
	public LoginUser() {
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
    
    
}
