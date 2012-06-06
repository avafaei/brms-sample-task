package com.citi.kyc.core.services.workflow;

public class CalculateRiskImpl {

	public static final Object lock = new Object();
	public static int count;
	
	public CalculateRiskImpl()
	{
		super();
	}

	public String search(String ssn, String lastName, String firstName)
	{
		//System.out.println("CalculateRiskImpl");
		synchronized(lock)
		{
			++count;
		}
		if (count %2 == 0) {
			return "low";
		} else if (count %3 == 0) {
			return "high";
		} else {
			return "medium";
		}
	}
}
