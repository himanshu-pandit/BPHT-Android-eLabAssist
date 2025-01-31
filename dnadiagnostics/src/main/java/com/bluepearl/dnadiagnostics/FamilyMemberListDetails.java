package com.bluepearl.dnadiagnostics;

public class FamilyMemberListDetails {

	private String PatientName;
	private String PatientId;
	private String Age;
	private String Gender;

	public FamilyMemberListDetails(String patientName, String id, String age, String gender) 
	{
		this.PatientName = patientName;
		this.PatientId = id;
		this.Age = age;
		this.Gender = gender;
	}

	public FamilyMemberListDetails() 
	{
		// TODO Auto-generated constructor stub
	}

	public void setPatientName(String pname) 
	{
		this.PatientName = pname;
	}

	public void setPatientId(String pid) 
	{
		this.PatientId = pid;
	}

	public void setAge(String page) 
	{
		this.Age = page;
	}

	public void setGender(String pgender) 
	{
		this.Gender = pgender;
	}

	public String getPatientName() 
	{
		return this.PatientName;
	}

	public String getPatientId() 
	{
		return this.PatientId;
	}

	public String getAge() 
	{
		return this.Age;
	}

	public String getGender() 
	{
		return this.Gender;
	}
}
