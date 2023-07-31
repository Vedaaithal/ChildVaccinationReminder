package com.example.childvaccinereminder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class add_child extends AppCompatActivity implements View.OnClickListener {

    private NotificationManagerCompat notificationManager;
    private TextInputEditText mName, mDob, mBloodGrp, mHeight, mWeight;
    FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private int age;
    FirebaseFirestore mStore;
    private long days;
    Calendar calendar;
    FloatingActionButton fbb;
    private RadioButton mRadioMale, mRadioFemale;
    private RadioGroup mRadioGroup;

    public add_child() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(this));
        mName = findViewById(R.id.et_name);
        mDob = findViewById(R.id.et_dob);
        mBloodGrp = findViewById(R.id.et_bloodGroup);
        mHeight = findViewById(R.id.et_height);
        mWeight = findViewById(R.id.et_weight);
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioMale = findViewById(R.id.radio_male);
        mRadioFemale = findViewById(R.id.radio_female);
        MaterialButton mAdd = findViewById(R.id.btn_add);
        calendar = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        fbb=findViewById(R.id.fbb);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        age = sharedPreferences.getInt("age", 0);

        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(add_child.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

            private void showDatePickerDialog() {
                DatePickerDialog datePickerDialog = new DatePickerDialog(add_child.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // The selected date will be set to the EditText field
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        mDob.setText(selectedDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        mAdd.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        mName.setError(null);
        mDob.setError(null);
        mBloodGrp.setError(null);
        mHeight.setError(null);
        mHeight.setError(null);
        validate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validate() {
        final String name = Objects.requireNonNull(mName.getText()).toString();
        final String dob = Objects.requireNonNull(mDob.getText()).toString();
        final String bloodGrp = Objects.requireNonNull(mBloodGrp.getText()).toString();
        final String height = Objects.requireNonNull(mHeight.getText()).toString();
        final String weight = Objects.requireNonNull(mWeight.getText()).toString();
        int selectedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        int flag = 0;

        String gender;
        if (selectedRadioButtonId == R.id.radio_male) {
            gender = "Male";
        } else if (selectedRadioButtonId == R.id.radio_female) {
            gender = "Female";
        } else {
            Toast.makeText(add_child.this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            mName.setError("Name is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(dob)) {
            mDob.setError("Date of birth is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(bloodGrp)) {
            mBloodGrp.setError("Blood group is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(height)) {
            mHeight.setError("Height is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(weight)) {
            mWeight.setError("Weight is required");
            flag = 1;
        }

        if (flag == 1) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Date strDate = null;
        try {
            strDate = sdf.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().before(strDate)) {
            mDob.setError("Invalid date of birth");
            return;
        }

        Toast toast = Toast.makeText(add_child.this, "Passwords do not match", Toast.LENGTH_SHORT);
        String[] strDOB = dob.split("/");
        LocalDate now = LocalDate.now();
        LocalDate birthDate = LocalDate.of(Integer.parseInt(strDOB[2]), Integer.parseInt(strDOB[1]), Integer.parseInt(strDOB[0]));
        days = ChronoUnit.DAYS.between(birthDate, now);

        sharedPreferences.edit().putInt("age", (int) days).apply();


        Intent serviceIntent = new Intent(this, AgeUpdateService.class);
        startService(serviceIntent);

        DocumentReference documentReference = mStore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("child").document(name);
        Map<String, Object> child = new HashMap<>();
        child.put("cName", name);
        child.put("dob", dob);
        child.put("bloodGrp", bloodGrp);
        child.put("height", height);
        child.put("weight", weight);
        child.put("gender", gender);

        documentReference.set(child).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Child added Successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

    }

}