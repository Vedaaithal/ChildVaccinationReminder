package com.example.childvaccinereminder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NOTIFICATION_SHOWN = "notification_shown";
    CardView addChild;
    CardView schedule;
    CardView notify;
    CardView vaccineDet;
    CardView doctors;
    CardView logout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> childrenWithNotification;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        childrenWithNotification = new ArrayList<>();

        addChild=findViewById(R.id.add_child);
        schedule=findViewById(R.id.view_child);
        notify=findViewById(R.id.notify);
        vaccineDet=findViewById(R.id.Vaccine);
        doctors=findViewById(R.id.doctor);
        logout=findViewById(R.id.logout);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean notificationShown = sharedPreferences.getBoolean(PREF_NOTIFICATION_SHOWN, false);

        if (!notificationShown) {
            retrieveChildData();

            // Mark notification as shown
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_NOTIFICATION_SHOWN, true);
            editor.apply();
        }

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),add_child.class);
                startActivity(intent);
                finish();
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),view_child.class);
                startActivity(intent);
                finish();
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),notify.class);
                startActivity(intent);
                finish();
            }
        });

        vaccineDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Vaccine.class);
                startActivity(intent);
                finish();
            }
        });

        doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DoctorSearch.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("No", null);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetNotificationStatus(); // Reset notification status on logout
                navigateToLoginMenuActivity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        LinearLayout.LayoutParams negativeButtonParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        LinearLayout buttonBar = ((LinearLayout) positiveButton.getParent());
        buttonBar.removeView(negativeButton);
        buttonBar.removeView(positiveButton);
        buttonBar.addView(positiveButton, positiveButtonParams);
        buttonBar.addView(negativeButton, negativeButtonParams);
    }

    private void resetNotificationStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_NOTIFICATION_SHOWN, false);
        editor.apply();
    }

    private void navigateToLoginMenuActivity() {
        Intent intent = new Intent(MainActivity.this, login.class);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void retrieveChildData() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("child")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String childName = document.getString("cName");
                            String dob = document.getString("dob");

                            long ageInDays = calculateAgeInDays(dob);
                            long remainingDays = calculateRemainingDays(ageInDays);

                            if (remainingDays <= 1) {
                                childrenWithNotification.add(childName);
                            }
                        }

                        if (!childrenWithNotification.isEmpty()) {
                            showNotificationDialog(childrenWithNotification);
                        }
                    } else {
                        // Handle error retrieving child data
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long calculateAgeInDays(String dob) {
        long ageInDays = -1;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dobDate = format.parse(dob);

            Date currentDate = new Date();
            long diffInMillis = currentDate.getTime() - dobDate.getTime();
            ageInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            Log.e("ChildData", "Error parsing dob: ", e);
        }
        return ageInDays;
    }

    private long calculateRemainingDays(long ageInDays) {
        long remainingDays;
        if (ageInDays <= 42) {
            remainingDays = 42 - ageInDays;
        } else if (ageInDays <= 70) {
            remainingDays = 70 - ageInDays;
        } else if (ageInDays <= 98) {
            remainingDays = 98 - ageInDays;
        } else if (ageInDays <= 183) {
            remainingDays = 183 - ageInDays;
        } else if (ageInDays <= 274) {
            remainingDays = 274 - ageInDays;
        } else if (ageInDays <= 335) {
            remainingDays = 335 - ageInDays;
        } else if (ageInDays <= 365) {
            remainingDays = 365 - ageInDays;
        } else if (ageInDays <= 456) {
            remainingDays = 456 - ageInDays;
        } else if (ageInDays <= 517) {
            remainingDays = 517 - ageInDays;
        } else if (ageInDays <= 548) {
            remainingDays = 548 - ageInDays;
        } else if (ageInDays <= 730) {
            remainingDays = 730 - ageInDays;
        } else {
            remainingDays = 1460 - ageInDays;
        }
        return remainingDays;
    }

    private void showNotificationDialog(List<String> childrenWithNotification) {
        StringBuilder message = new StringBuilder("It's time for vaccination for the following children:\n\n");
        for (String childName : childrenWithNotification) {
            message.append("- ").append(childName).append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vaccination Reminder")
                .setMessage(message.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog dismissed
                    }
                })
                .setCancelable(false)
                .show();
    }
}
