package com.bluepearl.dnadiagnostics;

public class CollectionCenterDetails {
	private String CenterName;
	private String CenterId;

	public CollectionCenterDetails( String Name, String id) {
		this.CenterName = Name;
		this.CenterId = id;
	}

	public CollectionCenterDetails() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return this.CenterName;
	}

	public String getId() {
		return this.CenterId;
	}
	
	public void setName(String cname) {
		this.CenterName = cname;
	}

	public void setId(String cid) {
		this.CenterId = cid;
	}
}
