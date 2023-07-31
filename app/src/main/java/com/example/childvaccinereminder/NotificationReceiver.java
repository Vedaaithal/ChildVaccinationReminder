package com.example.childvaccinereminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String childName = intent.getStringExtra("childName");
        String vaccineDetails = intent.getStringExtra("vaccineDetails");

        // Display a toast message with the notification details
        Toast.makeText(context, "Vaccination Reminder: " + childName + "\n" + vaccineDetails, Toast.LENGTH_SHORT).show();
    }
}




