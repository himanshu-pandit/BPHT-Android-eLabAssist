package com.bluepearl.dnadiagnostics;

public class PhlebotomyAreaListDetails
{
	private String PatientName;
	private String DoctorName;
	private String LabCode;
	private String Bal_Amt;
	private String Date;
	private String testRegId;
	private String State;

	private String patientMbile;
	private String patientAdrs;
	private String CollectionCenterName;
	private String SelectedTest;
	private String TotalAmount;
	private String AmountPaid;


	//String TotalAmount = myObj.getTotalAmount();
	//String AmountPaid = myObj.getAmountPaid();

	public PhlebotomyAreaListDetails(String patientName, String doctorName, String labCode, String Amnt,
                                     String date, String TestRegId, String state,
                                     String patientMbile, String patientAdrs, String CollectionCenterName, String SelectedTest,
                                     String TotalAmount, String AmountPaid )
	{
		this.PatientName = patientName;
		this.DoctorName = doctorName;
		this.LabCode = labCode;
		this.Bal_Amt = Amnt;
		this.Date = date;
		this.testRegId = TestRegId;
		this.State = state;

		this.patientMbile = patientMbile;
		this.patientAdrs = patientAdrs;
		this.CollectionCenterName = CollectionCenterName;
		this.SelectedTest = SelectedTest;


		this.TotalAmount = TotalAmount;
		this.AmountPaid = AmountPaid;
	}

	public PhlebotomyAreaListDetails()
	{
		// TODO Auto-generated constructor stub
	}

	public void setPatientName(String pname) 
	{
		this.PatientName = pname;
	}

	public void setDoctorName(String dname) 
	{
		this.DoctorName = dname;
	}

	public void setLabCode(String lcode) 
	{
		this.LabCode = lcode;
	}

	public void setBal_Amt(String bamt) 
	{
		this.Bal_Amt = bamt;
	}

	public void setDate(String date) 
	{
		this.Date = date;
	}

	public void setTestRegId(String testRegId) 
	{
		this.testRegId = testRegId;
	}
	
	public void setState(String state) 
	{
		this.State = state;
	}


	public void setpatientMbile(String patientMbile)
	{
		this.patientMbile = patientMbile;
	}
	public void setpatientAdrs(String patientAdrs)
{
	this.patientAdrs = patientAdrs;
}
	public void setCollectionCenterName(String CollectionCenterName)
	{
		this.CollectionCenterName = CollectionCenterName;
	}
	public void setSelectedTest(String SelectedTest)
	{
		this.SelectedTest = SelectedTest;
	}
	public void setTotalAmount(String TotalAmount)
	{
		this.TotalAmount = TotalAmount;
	}
	public void setAmountPaid(String AmountPaid)
	{
		this.AmountPaid = AmountPaid;
	}



	public String getPatientName() 
	{
		return this.PatientName;
	}

	public String getDoctorName() 
	{
		return this.DoctorName;
	}

	public String getLabCode() 
	{
		return this.LabCode;
	}

	public String getBal_Amt() 
	{
		return this.Bal_Amt;
	}

	public String getDate() 
	{
		return this.Date;
	}

	public String getTestRegId() 
	{
		return this.testRegId;
	}
	
	public String getState() 
	{
		return this.State;
	}
	public String getpatientMbile()
	{
		return this.patientMbile;
	}
	public String getpatientAdrs()
	{
		return this.patientAdrs;
	}
	public String getCollectionCenterName()
	{
		return this.CollectionCenterName;
	}
	public String getSelectedTest()
	{
		return this.SelectedTest;
	}
	public String getTotalAmount()
	{
		return this.TotalAmount;
	}
	public String getAmountPaid()
	{
		return this.AmountPaid;
	}
}
