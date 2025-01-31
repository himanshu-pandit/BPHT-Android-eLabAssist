package com.bluepearl.dnadiagnostics;


public class LabSelectionDoctorDetails {

	private String LabName;
	private String LabAddress;
	private String LabId;
	private String LabRole;

	public LabSelectionDoctorDetails(String name, String address, String labid, String labrole) 
	{
		this.LabName = name;
		this.LabAddress = address;
		this.LabId = labid;
		this.LabRole = labrole;
	}

	public LabSelectionDoctorDetails() 
	{
		// TODO Auto-generated constructor stub
	}

	public void setLabName(String name) 
	{
		this.LabName = name;
	}

	public void setLabAddress(String addr) 
	{
		this.LabAddress = addr;
	}


	public void setLabId(String id) 
	{
		this.LabId = id;
	}

	public void setLabRole(String role) 
	{
		this.LabRole = role;
	}

	public String getLabName() 
	{
		return this.LabName;
	}

	public String getLabAddress() 
	{
		return this.LabAddress;
	}


	public String getLabId() 
	{
		return this.LabId;
	}
	
	public String getLabRole() 
	{
		return this.LabRole;
	}

}