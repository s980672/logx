package com.sktechx.palab.logx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1100449 on 2016. 10. 28..
 */
public class User {
	
	private String name;
	private String key;
	@SerializedName("first-name")
	private String firstName;
	@SerializedName("last-name")
	private String lastName;
	@SerializedName("display-name")
	private String displayName;
	private String email;
	private String token;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
