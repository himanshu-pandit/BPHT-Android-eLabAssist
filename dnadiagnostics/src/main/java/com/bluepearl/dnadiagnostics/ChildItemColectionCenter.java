package com.bluepearl.dnadiagnostics;

import java.util.Comparator;

public class ChildItemColectionCenter
{
	private String SelectedTest;
	private String SelectedCenterName;
	private String SelectedCenterAdres;
    private String SelectedCenterLongitude;
    private String SelectedCenterLatitude;
	private String SelectedCenterID;
	private String SelectedCenterAreaDrName;


	public String getSelectedTest()
	{
		return SelectedTest;
	}


	public String getSelectedCenterName()
	{
		return SelectedCenterName;
	}

	public String getSelectedCenterAdress()
	{
		return SelectedCenterAdres;
	}



    public String getSelectedCenterLongitude()
    {
        return SelectedCenterLongitude;
    }

    public String getSelectedCenterLatitude()
    {
        return SelectedCenterLatitude;
    }
	public String getSelectedCenterAreaDrName()
	{
		return SelectedCenterAreaDrName;
	}

	public String getSelectedCenterID()
	{
		return SelectedCenterID;
	}


	public void setSelectedTest(String selectedTest)
	{
		SelectedTest = selectedTest;
	}

	public void setSelectedCenmterName(String selectedCenterName)
	{
		SelectedCenterName = selectedCenterName;
	}

	public void setSelectedCenterAdress(String selectedCenterAdres)
	{
		SelectedCenterAdres = selectedCenterAdres;
	}

    public void setSelectedCenterLongitude (String selectedCenterLongitude)
    {
        SelectedCenterLongitude = selectedCenterLongitude;
    }

    public void setSelectedCenterLatitude(String selectedCenterLatitude)
    {
        SelectedCenterLatitude = selectedCenterLatitude;
    }

	public void setSelectedCenterID(String selectedCenterID)
	{
		SelectedCenterID = selectedCenterID;
	}

	public void setSelectedCenterAreaDrName(String selectedCenterAreaDrName)
	{
		SelectedCenterAreaDrName = selectedCenterAreaDrName;
	}



    /*Comparator for sorting the list by Student Name*/
    public static Comparator<ChildItemColectionCenter> StuNameClidComparator = new Comparator<ChildItemColectionCenter>() {

        public int compare(ChildItemColectionCenter s1, ChildItemColectionCenter s2) {
            String StudentName1 = s1.getSelectedCenterAreaDrName().toUpperCase();
            String StudentName2 = s2.getSelectedCenterAreaDrName().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};


}
