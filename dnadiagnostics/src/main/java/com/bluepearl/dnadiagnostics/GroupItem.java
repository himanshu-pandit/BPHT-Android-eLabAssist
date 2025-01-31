package com.bluepearl.dnadiagnostics;

import java.util.ArrayList;

public class GroupItem 
{
	public String AppointmentDate;
	public String AppointmentAddress;
	public ArrayList<ChildItem> ChildItems;

	public String getAppointmentDate() 
	{
		return AppointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) 
	{
		AppointmentDate = appointmentDate;
	}

	public String getAppointmentAddress() 
	{
		return AppointmentAddress;
	}

	public void setAppointmentAddress(String appointmentAddress) 
	{
		AppointmentAddress = appointmentAddress;
	}

	public ArrayList<ChildItem> getChildItems()
	{
		return ChildItems;
	}

	public void setChildItems(ArrayList<ChildItem> childItems)
	{
		ChildItems = childItems;
	}
}
