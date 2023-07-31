package com.example.childvaccinereminder;

import android.widget.ListView;

public class Versions {

    String vaccine_name,description;
    boolean isVisible;


    public Versions(String vaccine_name, String description,boolean isVisible) {
        this.vaccine_name=vaccine_name;
        this.description=description;
        this.isVisible=isVisible;
    }


}
