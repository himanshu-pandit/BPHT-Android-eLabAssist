package com.bluepearl.dnadiagnostics;

public class PatientLabSelectionDetailes {
    private String email;
    private String phone;

    private String LabName;
    private String LabAddress;
    private String Distance;
    private String LabId;
    private String register;
    private String preferred;
    private String homecollection;
    private  String LabMobileNumber;
    private String LabEmailId;
    public PatientLabSelectionDetailes(String name, String address, String distance, String labid, String regflg, String pref_flg, String home_flg)
    {
        this.LabName = name;
        this.LabAddress = address;
        this.Distance = distance;
        this.LabId = labid;
        this.register = regflg;
        this.preferred = pref_flg;
        this.homecollection = home_flg;
    }
    public PatientLabSelectionDetailes(String name, String address, String distance, String labid, String regflg, String pref_flg, String home_flg,String lmn,String le)
    {
        this.LabName = name;
        this.LabAddress = address;
        this.Distance = distance;
        this.LabId = labid;
        this.register = regflg;
        this.preferred = pref_flg;
        this.homecollection = home_flg;
        this.LabMobileNumber=lmn;
        this.LabEmailId=le;
    }
    public PatientLabSelectionDetailes()
    {
        // TODO Auto-generated constructor stub
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setLabName(String name)
    {
        this.LabName = name;
    }

    public void setLabAddress(String addr)
    {
        this.LabAddress = addr;
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

    public String getLabName()
    {
        return this.LabName;
    }

    public String getLabAddress()
    {
        return this.LabAddress;
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

    public String getLabEmailId() {
        return LabEmailId;
    }

    public void setLabEmailId(String labEmailId) {
        LabEmailId = labEmailId;
    }

    public String getLabMobileNumber() {
        return LabMobileNumber;
    }

    public void setLabMobileNumber(String labMobileNumber) {
        LabMobileNumber = labMobileNumber;
    }
}