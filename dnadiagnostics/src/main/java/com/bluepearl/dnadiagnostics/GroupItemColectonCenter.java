package com.bluepearl.dnadiagnostics;

import java.util.ArrayList;
import java.util.Comparator;

public class GroupItemColectonCenter
{
	public String AppointmentDate;
	public String AppointmentAddress;
	public ArrayList<ChildItemColectionCenter> ChildItems;

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

	public ArrayList<ChildItemColectionCenter> getChildItems()
	{
		return ChildItems;
	}

	public void setChildItems(ArrayList<ChildItemColectionCenter> childItems)
	{
		ChildItems = childItems;
	}

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<GroupItemColectonCenter> StuNameGrouopComparator = new Comparator<GroupItemColectonCenter>() {

        public int compare(GroupItemColectonCenter s1, GroupItemColectonCenter s2) {
            String StudentName1 = s1.getAppointmentAddress().toUpperCase();
            String StudentName2 = s2.getAppointmentAddress().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};


}
