package com.example.childvaccinereminder;

public class DataClass {

    private String cName;
    private String dob;
    private String bloodGrp;
    private String height;
    private String weight;
    private String gender;

    public DataClass(String childName,String dob,String bldgrp,String height,String weight,String gender) {
        this.cName = childName;
        this.dob=dob;
        this.bloodGrp=bldgrp;
        this.weight=weight;
        this.height=height;
        this.gender=gender;
    }

    public String getDob() {
        return dob;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getcName() {
        return cName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }


    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public DataClass(){}
}
