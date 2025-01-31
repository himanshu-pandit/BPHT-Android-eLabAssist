package com.bluepearl.dnadiagnostics;

public class DoctorNameDetails {
	private String DoctorName;
	private String DoctorId;

	public DoctorNameDetails(String doctorName, String doctorId) {
		this.DoctorName = doctorName;
		this.DoctorId = doctorId;
	}

	public DoctorNameDetails() {
		// TODO Auto-generated constructor stub
	}


	public String getUserName() {
		return this.DoctorName;
	}

	public String getUserId() {
		return this.DoctorId;
	}
	
	public void setUserName(String name) {
		this.DoctorName = name;
	}

	public void setUserId(String id) {
		this.DoctorId = id;
	}
}
