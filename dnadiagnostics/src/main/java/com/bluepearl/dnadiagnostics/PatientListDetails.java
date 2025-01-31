package com.bluepearl.dnadiagnostics;

public class PatientListDetails 
{
	private String PatientName;
	private String DoctorName;
	private String Date;
	private String Bal_Amt;
	private String testRegId;
	private String LabCode;
	private String testState;

	public PatientListDetails(String patientname, String doctorName,String labCode,
							  String Amnt, String date, String TestRegId,
							  String testState)
	{
		this.PatientName = patientname;
		this.DoctorName = doctorName;
		this.LabCode = labCode;
		this.Bal_Amt = Amnt;
		this.Date = date;
		this.testRegId = TestRegId;
		this.testState = testState;
	}

	public PatientListDetails()
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
	public void setBal_Amt(String bamt)
	{
		this.Bal_Amt = bamt;
	}
	public void setDate(String date)
	{
		this.Date = date;
	}
	public void setLabCode(String lcode)
	{
		this.LabCode = lcode;
	}

	public void setTestRegId(String testRegId)
	{
		this.testRegId = testRegId;
	}
	public void setTestState(String testState)
	{
		this.testState = testState;
	}
	public String getPatientName()
	{
		return this.PatientName;
	}
	public String getLabCode()
	{
		return this.LabCode;
	}


	public String getDoctorName()
	{
		return this.DoctorName;
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
	public String getTestState()
	{
		return this.testState;
	}
}

