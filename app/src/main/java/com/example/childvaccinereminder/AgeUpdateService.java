package com.example.childvaccinereminder;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Objects;

public class AgeUpdateService extends Service {

    private int age;
    private NotificationManagerCompat notificationManager;
    private static BroadcastReceiver dateBroadcastReceiver;

    @Override
    public void onCreate() {
        registerDateChangeReceiver();
        notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(this));
        Toast.makeText(this, "Service running", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(dateBroadcastReceiver);
        dateBroadcastReceiver = null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void registerDateChangeReceiver() {
        dateBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                age = sharedPreferences.getInt("age", -1);
                age++;
                sharedPreferences.edit().putInt("age", age).apply();
                checkVaccine();
                Toast.makeText(context, "Date changed", Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        registerReceiver(dateBroadcastReceiver, filter);
    }

    private void checkVaccine() {
        if (age == 0 || age == 42 || age == 70 || age == 98 || age == 183 || age == 274 || age == 335 || age == 365 || age == 456 || age == 517 || age == 548 || age == 730 || age == 1460) {
            notifyUser();
        }
    }

    private void notifyUser() {
        // Create an explicit intent for an Activity
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification builder

        // Show the notification
        int notificationId = 1;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }
}