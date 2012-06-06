package com.citi.kyc.core.services.workflow;

public class SearchImpl 
{	
	public SearchImpl()
	{
		super();
	}

	
	public String search(String ssn, String firstName, String lastName )
	{
		System.out.println("SearchImpl: ssn: " + ssn +"\t last: " + lastName + "\t first: " + firstName);
		if (ssn != null && firstName != null && lastName != null) {
			return "no";
		} else {
			return "yes";
		}		
	}
}
