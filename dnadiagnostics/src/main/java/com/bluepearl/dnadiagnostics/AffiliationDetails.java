package com.bluepearl.dnadiagnostics;

public class AffiliationDetails {
	private String AffiliationName;
	private String AffiliationId;

	public AffiliationDetails(String Name, String id) {
		this.AffiliationName = Name;
		this.AffiliationId = id;
	}

	public AffiliationDetails() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return this.AffiliationName;
	}

	public String getId() {
		return this.AffiliationId;
	}
	
	public void setName(String cname) {
		this.AffiliationName = cname;
	}

	public void setId(String cid) {
		this.AffiliationId = cid;
	}
}
