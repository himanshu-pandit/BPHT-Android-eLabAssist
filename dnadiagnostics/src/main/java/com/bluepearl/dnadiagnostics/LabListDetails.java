package com.bluepearl.dnadiagnostics;

public class LabListDetails 
{
	private String PatientName;
	private String DoctorName;
	private String LabCode;
	private String Bal_Amt;
	private String Date;
	private String testRegId;
	private String State;
	private String SlectTest;
	private String TestListDetails;
	private String TotalTest;
	private String ApproveTest;
	private String AMobile;
	private String CCMobile;
	private String DMobile;
	private String PMobile;
	public LabListDetails(String patientName, String doctorName, String labCode, String Amnt, String date, String TestRegId, String state,
						  String selecttest, String testlistdetails, String totalTest, String approveTest, String aMobile, String ccMobile, String dMobile, String pMobile)
	{
		this.PatientName = patientName;
		this.DoctorName = doctorName;
		this.LabCode = labCode;
		this.Bal_Amt = Amnt;
		this.Date = date;
		this.testRegId = TestRegId;
		this.State = state;
		this.SlectTest = selecttest;
		this.TestListDetails = testlistdetails;
		this.TotalTest = totalTest;
		this.ApproveTest = approveTest;
		this.AMobile = aMobile;
		this.CCMobile = ccMobile;
		this.DMobile = dMobile;
		this.PMobile = pMobile;
	}

	public LabListDetails() 
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
	public void setTestListDetails(String testlistdetails)
	{
		this.TestListDetails = testlistdetails;
	}

	public void setTestName(String selectState)
	{
		this.SlectTest = selectState;
	}	public void setApproveTest(String approveTest)
{
	this.ApproveTest = approveTest;
}
	public void setTotalTest(String totalTest)
	{
		this.TotalTest = totalTest;
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

	public String getSelectTest()
	{
		return this.SlectTest;
	}

	public String getTestListDetails()
	{
		return this.TestListDetails;
	}
	public String getApproveTest()
	{
		return this.ApproveTest;
	}
	public String getTotalTest()
	{
		return this.TotalTest;
	}

	public String getAMobile() {
		return AMobile;
	}

	public void setAMobile(String AMobile) {
		this.AMobile = AMobile;
	}

	public String getCCMobile() {
		return CCMobile;
	}

	public void setCCMobile(String CCMobile) {
		this.CCMobile = CCMobile;
	}

	public String getDMobile() {
		return DMobile;
	}

	public void setDMobile(String DMobile) {
		this.DMobile = DMobile;
	}

	public String getPMobile() {
		return PMobile;
	}

	public void setPMobile(String PMobile) {
		this.PMobile = PMobile;
	}
}
