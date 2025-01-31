package com.bluepearl.dnadiagnostics;

public class TsetListDetails {

	private String TestName;
	private String TestStateus;
	private String Distance;
	private String LabId;
	private String register;
	private String preferred;
	private String homecollection;

	public TsetListDetails(String name, String teststat)
	//public TsetListDetails(String name, String teststat, String distance, String labid, String regflg, String pref_flg, String home_flg)

	{
		this.TestName = name;
		this.TestStateus = teststat;
/*		this.Distance = distance;
		this.LabId = labid;
		this.register = regflg;
		this.preferred = pref_flg;
		this.homecollection = home_flg;*/
	}

	public TsetListDetails()
	{
		// TODO Auto-generated constructor stub
	}

	public void setLabName(String name) 
	{
		this.TestName = name;
	}

	public void settestStatus(String addr)
	{
		this.TestStateus = addr;
	}

	public void setLabDistance(String dist) 
	{
		this.Distance = dist;
	}

	public void setLabId(String id) 
	{
		this.LabId = id;
	}

	public void setregister(String id) 
	{
		this.register = id;
	}

	public void setpreferred(String id) 
	{
		this.preferred = id;
	}

	public void sethomeflg(String hflg) 
	{
		this.homecollection = hflg;
	}

	public String gettestName()
	{
		return this.TestName;
	}

	public String getTestStatus()
	{
		return this.TestStateus;
	}

	public String getLabDistance() 
	{
		return this.Distance;
	}

	public String getLabId() 
	{
		return this.LabId;
	}

	public String getregister() 
	{
		return this.register;
	}

	public String getpreferred() 
	{
		return this.preferred;
	}

	public String gethomeflg() 
	{
		return this.homecollection;
	}
}


