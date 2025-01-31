package com.bluepearl.dnadiagnostics;

public class PreferredLabNameDetails {
	private String labName;
	private String labid;

	public PreferredLabNameDetails( String Name, String id) {
		this.labName = Name;
		this.labid = id;
	}

	public PreferredLabNameDetails() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return this.labName;
	}

	public String getId() {
		return this.labid;
	}
	
	public void setName(String lname) {
		this.labName = lname;
	}

	public void setId(String lid) {
		this.labid = lid;
	}
}
