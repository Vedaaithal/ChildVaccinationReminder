package com.example.childvaccinereminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.constraintlayout.widget.Constraints.TAG;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {

    TextView textView;
    TextInputEditText editTextEmail, editTextPassword,editTextPhone,editTextName,editTextRePassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore mStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        {
            setContentView(R.layout.activity_register);

            if (getSupportActionBar() != null) {
                getSupportActionBar();
            }
            editTextEmail = findViewById(R.id.email);
            editTextPassword = findViewById(R.id.password);
            editTextName = findViewById(R.id.name);
            editTextPhone = findViewById(R.id.number);
            editTextRePassword=findViewById(R.id.rePassword);
            buttonReg = findViewById(R.id.btn_signup);
            mAuth = FirebaseAuth.getInstance();
            mStore = FirebaseFirestore.getInstance();
            progressBar = findViewById(R.id.progressBar);



            textView = findViewById(R.id.LoginNow);


            buttonReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    String email, password,fullName,mobile,rePassword;
                    email = editTextEmail.getText().toString().trim();
                    password = editTextPassword.getText().toString().trim();
                    fullName=editTextName.getText().toString().trim();
                    mobile=editTextPhone.getText().toString().trim();
                    rePassword=editTextRePassword.getText().toString().trim();


                    if (TextUtils.isEmpty(email)) {
                        editTextEmail.setError("Email is required");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        editTextPassword.setError("Password is required");
                        return;
                    }
                    if (password.length() < 8) {
                        editTextPassword.setError("Password must atleast be 8 characters");
                        return;
                    }
                    if (rePassword.length() < 8) {
                        editTextPassword.setError("Password must atleast be 8 characters");
                        return;
                    }
                    if(TextUtils.isEmpty(rePassword)){
                        editTextRePassword.setError("Confirm your password");
                        return;
                    }
                    if(TextUtils.isEmpty(fullName)){
                        editTextName.setError("Full Name is required");
                        return;
                    }
                    if(mobile.length()<10){
                        editTextPhone.setError("Enter a valid mobile");
                        return;
                    }
                    if(!password.equals(rePassword)){
                        Toast toast = Toast.makeText(register.this,"Passwords do not match", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(register.this, "User is Registered Successfully", Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference=mStore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                                Map<String,Object> user=new HashMap<>();
                                user.put("fName",fullName);
                                user.put("email",email);
                                user.put("mobile",mobile);


                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: User Profile created");
                                        startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure: "+e.toString());
                                    }
                                });
                                progressBar.setVisibility(View.INVISIBLE);


                            } else {
                                Toast.makeText(register.this, "Registration failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), login.class));
                }
            });
        }
    }
}