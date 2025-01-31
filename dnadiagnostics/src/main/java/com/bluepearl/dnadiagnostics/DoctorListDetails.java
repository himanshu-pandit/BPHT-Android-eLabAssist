package com.bluepearl.dnadiagnostics;

public class DoctorListDetails 
{


	private String PatientName;
	private String LabCode;
	private String Date;
	private String Bal_Amt;
	private String testRegId;
	private String State;
	private String DoctorRelease;
	private String PatientRelease;
	private String DoctorName;

	private String TestListDetails;
	private String TotalTest;
	private String ApproveTest;
	private String AMobile;
	private String CCMobile;
	private String DMobile;
	private String PMobile;
	public DoctorListDetails(String patientName, String labCode, String Amnt,
							 String state,String date, String doctorName,String TestRegId,
							 String doctorReles, String PatientReles,String testlistdetails,String totalTest,String approveTest,String aMobile, String ccMobile, String dMobile, String pMobile)
	{
		this.PatientName = patientName;
		this.LabCode = labCode;
		this.Bal_Amt = Amnt;
		this.Date = date;
		this.testRegId = TestRegId;
		this.DoctorName = doctorName;
		this.State = state;
		this.DoctorRelease = doctorReles;
		this.PatientRelease = PatientReles;
		this.TestListDetails = testlistdetails;
		this.TotalTest = totalTest;
		this.ApproveTest = approveTest;
		this.AMobile = aMobile;
		this.CCMobile = ccMobile;
		this.DMobile = dMobile;
		this.PMobile = pMobile;
	}

	public DoctorListDetails()
	{
		// TODO Auto-generated constructor stub
	}

	public void setPatientName(String pname)
	{
		this.PatientName = pname;
	}

	public void setLabCode(String lcode)
	{
		this.LabCode = lcode;
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

	public void setTestRegId(String testRegId)
	{
		this.testRegId = testRegId;
	}
	public String getPatientRelease() {
		return PatientRelease;
	}

	public void setReleaserPatient(String patientRelease) {
		PatientRelease = patientRelease;
	}
	public void setTestListDetails(String testlistdetails)
	{
		this.TestListDetails = testlistdetails;
	}

	public void setApproveTest(String approveTest)
	{
		this.ApproveTest = approveTest;
	}

	public void setTotalTest(String totalTest)
	{
		this.TotalTest = totalTest;
	}



	public String getDoctorRelease() {
		return DoctorRelease;
	}

	public void setReleaserDoctor(String doctorRelease) {
		DoctorRelease = doctorRelease;
	}

	public String getState() {
		return State;

	}

	public void setState(String state) {
		State = state;
	}

	public String getPatientName()
	{
		return this.PatientName;
	}
	public String getBal_Amt()
	{
		return this.Bal_Amt;
	}
	public String getLabCode()
	{
		return this.LabCode;
	}

	public String getDoctorName()
	{
		return this.DoctorName;
	}

	public String getDate()
	{
		return this.Date;
	}

	public String getTestRegId()
	{
		return this.testRegId;
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