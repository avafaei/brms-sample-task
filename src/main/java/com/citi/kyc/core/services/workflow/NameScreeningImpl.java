package com.citi.kyc.core.services.workflow;

public class NameScreeningImpl 
{
	public static final Object lock = new Object();
	public static int count;
	
	private String nameCase;
	
	private long recordId;
	
	public NameScreeningImpl()
	{
		super();
		//recordId = 0l;
	}

	public String search(String ssn, String lastName, String firstName, String recordID)
	{
		try {
			recordId = Long.parseLong(recordID);
		} catch (Exception e){
			//e.printStackTrace();
		}
		if (recordId != 0l) {
			return "yes";
		}
		synchronized(lock)
		{
			++count;
		}
		if (count %2 == 0) {
			nameCase = "yes";
			return nameCase;
		} else {
			nameCase = "no";
			return nameCase;
		}
	}
	
	public String getRecordId()
	{
		if (recordId != 0l) return recordId +"";
		if ("yes".equals(nameCase)) {
			long recordId = (long) (Math.random() * 10000 + 1);
			return recordId + "";
		}
		return "0";
	}
}
