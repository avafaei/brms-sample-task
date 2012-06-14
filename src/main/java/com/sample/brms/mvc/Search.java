package com.sample.brms.mvc;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Search {
	
	
	@NotNull
	@Size(min=1, max=25)
	private String firstName;
	
	@NotNull
	@Size(min=1, max=25)
	private String lastName;
	
	@NotNull
	@Size(min=9, max=9)
	private String ssn;

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

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	
	
	
	
}