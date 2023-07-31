package com.example.childvaccinereminder;

public class Doctor {
    private String name;
    private String qualification;
    private String contact;
    private String address;

    public Doctor(String name, String qualification, String contact, String address) {
        this.name = name;
        this.qualification = qualification;
        this.contact = contact;
        this.address = address;
    }
    public String getName() {
        return name;
    }

    public String getQualification() {
        return qualification;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }
}
