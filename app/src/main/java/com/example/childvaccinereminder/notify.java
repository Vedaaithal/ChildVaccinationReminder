package com.example.childvaccinereminder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class notify extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChildAdapter childAdapter;
    private List<Child> childList;
    private FirebaseFirestore db;
    FloatingActionButton fbb;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private static final String CHANNEL_ID = "VaccinationReminderChannel";
    private static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        childList = new ArrayList<>();
        childAdapter = new ChildAdapter(childList);
        recyclerView.setAdapter(childAdapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        if (mAuth.getCurrentUser() != null) {
            retrieveChildData();

            // Schedule reminders for each child
            for (Child child : childList) {
                scheduleReminder(child);
            }
        } else {
            promptLogin();
        }

        fbb=findViewById(R.id.fbb);

        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(notify.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void promptLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Required")
                .setMessage("Please log in to view vaccination reminders.")
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start the LoginActivity or any other activity for login
                        Intent intent = new Intent(notify.this, login.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action if needed
                        finish(); // Close the activity if the user cancels login
                    }
                })
                .setCancelable(false)
                .show();
    }
    private void retrieveChildData() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users")
                .document(userId)
                .collection("child")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String childName = document.getString("cName");
                                String dob = document.getString("dob");

                                long ageInDays = calculateAgeInDays(dob);
                                long remainingDays = calculateRemainingDays(ageInDays);

                                Child child = new Child(childName, ageInDays, remainingDays);
                                childList.add(child);
                                childAdapter.notifyDataSetChanged();

                                if (child.getRemainingDays() <= 1) {
                                    displayVaccinationReminder(child);
                                }
                            }
                        } else {
                            Log.e("ChildData", "Error getting child data: ", task.getException());
                        }
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

    private void displayVaccinationReminder(Child child) {
        String vaccineDetails = getVaccineDetails(child.getAge());

        // Show vaccination reminder as a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vaccination Reminder")
                .setMessage("It's time for " + child.getName() + "'s vaccination!\n\n" + vaccineDetails)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    private void scheduleReminder(final Child child) {
        // Calculate the time for the reminder (30 minutes before vaccination)
        long reminderTimeMillis = calculateReminderTime(child.getRemainingDays());

        // Create an intent to handle the notification click
        Intent intent = new Intent(this, notify.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Vaccination Reminder")
                .setContentText(child.getName() + "'s vaccination is coming up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Schedule the notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(notify.this, MainActivity.class);
        intent.putExtra("childList", new ArrayList<>(childList)); // Pass the childList to MainActivity
        startActivity(intent);
    }

    private long calculateReminderTime(long remainingDays) {
        // Calculate the reminder time (30 minutes before vaccination date)
        long millisecondsPerDay = 24 * 60 * 60 * 1000; // Number of milliseconds in a day
        long reminderTimeMillis = remainingDays * millisecondsPerDay - TimeUnit.MINUTES.toMillis(30);
        return System.currentTimeMillis() + reminderTimeMillis;
    }

// ...

    @Override
    protected void onResume() {
        super.onResume();

        // Create the notification channel (required for Android Oreo and above)
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Vaccination Reminder";
            String description = "Channel for vaccination reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private class Child {
        private String name;
        private long age;
        private long remainingDays;

        public Child(String name, long age,long remainingDays) {
            this.name = name;
            this.age = age;
            this.remainingDays=remainingDays;
        }

        public String getName() {
            return name;
        }

        public long getAge() {
            return age;
        }
        public long getRemainingDays() {
            return remainingDays;
        }
    }

    private String getVaccineDetails(long age) {
        StringBuilder vaccineDetails = new StringBuilder();
        //if (age <= 0)
        //vaccineDetails.append("Vaccine Details Not Applicable.");
        if (age <= 42) {
            vaccineDetails.append("Vaccine Details for Week 6:");
            vaccineDetails.append("\n- Diptheria, Tetanus and Pertussis vaccine (DTwP 1)");
            vaccineDetails.append("\n- Inactivated polio vaccine (IPV 1)");
            vaccineDetails.append("\n- Hepatitis B  (Hep – B2)");
            vaccineDetails.append("\n- Haemophilus influenzae type B (Hib 1)");
            vaccineDetails.append("\n- Rotavirus 1");
            vaccineDetails.append("\n- Pneumococcal conjugate vaccine (PCV 1)");
        } else if (age <= 70) {
            vaccineDetails.append("Vaccine Details for Week 10:");
            vaccineDetails.append("\n- Diptheria, Tetanus and Pertussis vaccine (DTwP 2)");
            vaccineDetails.append("\n- Inactivated polio vaccine (IPV 2)");
            vaccineDetails.append("\n- Haemophilus influenzae type B (Hib 2)");
            vaccineDetails.append("\n- Rotavirus 2");
            vaccineDetails.append("\n- Pneumococcal conjugate vaccine (PCV 2)");
        } else if (age <= 98) {
            vaccineDetails.append("Vaccine Details for Week 14:");
            vaccineDetails.append("\n- Diptheria, Tetanus and Pertussis vaccine (DTwP 3)");
            vaccineDetails.append("\n- Inactivated polio vaccine (IPV 3)");
            vaccineDetails.append("\n- Haemophilus influenzae type B (Hib 3)");
            vaccineDetails.append("\n- Rotavirus 3");
            vaccineDetails.append("\n- Pneumococcal conjugate vaccine (PCV 3)");

        } else if (age <= 183) {
            vaccineDetails.append("Vaccine Details for Month 6:");
            vaccineDetails.append("\n- Oral polio vaccine (OPV 1)");
            vaccineDetails.append("\n- Hepatitis B (Hep – B3)");

        } else if (age <= 274) {
            vaccineDetails.append("Vaccine Details for Month 9:");
            vaccineDetails.append("\n- Oral polio vaccine (OPV 2)");
            vaccineDetails.append("\n- Measles, Mumps, and Rubella (MMR – 1)");

        } else if (age <= 335) {
            vaccineDetails.append("Vaccine Details for Months 9-12:");
            vaccineDetails.append("\n- Typhoid Conjugate Vaccine");

        } else if (age <= 365) {
            vaccineDetails.append("Vaccine Details for Month 12:");
            vaccineDetails.append("\n- Hepatitis A (Hep – A1)");
        } else if (age <= 456) {
            vaccineDetails.append("Vaccine Details for Month 15:");
            vaccineDetails.append("\n- Measles, Mumps, and Rubella (MMR 2)");
            vaccineDetails.append("\n- Varicella 1");
            vaccineDetails.append("\n- PCV booster");
        } else if (age <= 517) {
            vaccineDetails.append("Vaccine Details for Months 16-18:");
            vaccineDetails.append("\n- Diphtheria, Perussis, and Tetanus (DTwP B1/DTaP B1)");
            vaccineDetails.append("\n- Inactivated polio vaccine (IPV B1)");
            vaccineDetails.append("\n- Haemophilus influenzae type B (Hib B1)");
        } else if (age <= 548) {
            vaccineDetails.append("Vaccine Details for Month 18:");
            vaccineDetails.append("\n- Hepatitis A (Hep – A2)");
        } else if (age <= 730) {
            vaccineDetails.append("Vaccine Details for Year 2:");
            vaccineDetails.append("\n- Booster of Typhoid Conjugate Vaccine");
        } else {
            vaccineDetails.append("Vaccine Details for Years 4-6:");
            vaccineDetails.append("\n- Diphtheria, Perussis, and Tetanus (DTwP B2/DTaP B2)");
            vaccineDetails.append("\n- Oral polio vaccine (OPV 3)");
            vaccineDetails.append("\n- Varicella 2");
            vaccineDetails.append("\n- Measles, Mumps, and Rubella (MMR 3)");
        }
        return vaccineDetails.toString();
    }
    private class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
        private List<Child> childList;

        public ChildAdapter(List<Child> childList) {
            this.childList = childList;
        }

        @NonNull
        @Override
        public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_card, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
            Child child = childList.get(position);
            holder.childNameTextView.setText(child.getName());
            holder.ageTextView.setText(child.getAge() + " days old");
            holder.remainDaysTextView.setText("Remaining Days: " + child.getRemainingDays());
            holder.upVaccineTextView.setText(getVaccineDetails(child.getAge()));

        }

        @Override
        public int getItemCount() {
            return childList.size();
        }

        private class ChildViewHolder extends RecyclerView.ViewHolder {

            private TextView childNameTextView;
            private TextView remainDaysTextView;
            private TextView ageTextView;
            private TextView upVaccineTextView;

            public ChildViewHolder(@NonNull View itemView) {
                super(itemView);
                childNameTextView = itemView.findViewById(R.id.childNameTextView);
                ageTextView = itemView.findViewById(R.id.ageTextView);
                remainDaysTextView = itemView.findViewById(R.id.remainDaysTextView);
                upVaccineTextView = itemView.findViewById(R.id.upVaccineTextView);
            }
        }
    }
}

