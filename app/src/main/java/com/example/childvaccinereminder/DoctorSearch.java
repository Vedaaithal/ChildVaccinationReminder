package com.example.childvaccinereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DoctorSearch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    private List<Doctor> doctorList;
    FloatingActionButton fbb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorList = new ArrayList<>();
        adapter = new DoctorAdapter(doctorList);
        recyclerView.setAdapter(adapter);
        fbb=findViewById(R.id.fbb);

        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoctorSearch.this,MainActivity.class);
                startActivity(intent);
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        doctorList.clear();

        if (query.isEmpty()) {
            doctorList.addAll(getDoctorData());
        } else {
            for (Doctor doctor : getDoctorData()) {
                if (doctor.getAddress().toLowerCase().contains(query.toLowerCase())) {
                    doctorList.add(doctor);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private List<Doctor> getDoctorData() {
        List<Doctor> doctors = new ArrayList<>();

        doctors.add(new Doctor("Dr. Sanjay Kumar", "MBBS, DNB", "+91 87654 32109", "456 Jayanagar, Mysore, Karnataka"));
        doctors.add(new Doctor("Dr. Rahul Gupta", "MD, DCH", "+91 43210 98765", "901 Jayanagar, Mysore, Karnataka"));
        doctors.add(new Doctor("Dr. Ritu Patel", "MD, DCH", "+91 76543 21098", "789 Malleshwaram, Bangalore, Karnataka"));
        doctors.add(new Doctor("Dr. Arjun Desai", "MD, DCH", "+91 21098 76543", "890 Malleshwaram, Bangalore, Karnataka"));
        doctors.add(new Doctor("Dr. Manoj Kumar", "MD, MRCPCH", "+91 65432 10987", "234 Koramangala, Bangalore, Karnataka"));
        doctors.add(new Doctor("Dr. Karthik Kumar", "MD, DCH", "+91 09876 54321", "345 Koramangala, Bangalore, Karnataka"));
        doctors.add(new Doctor("Dr. Neha Singh", "MBBS, DCH", "+91 87654 32109", "678 Indiranagar, Belgaum, Karnataka"));
        doctors.add(new Doctor("Dr. Priya Sharma", "MD, DCH", "+91 87654 32109", "123 Indiranagar, Belgaum, Karnataka"));
        doctors.add(new Doctor("Dr. Vinay Kumar", "MD, DCH", "+91 09876 54321", "345 Ankadkatte, Kundapur, Udupi"));
        doctors.add(new Doctor("Dr. Sunita Reddy", "MBBS, DNB", "+91 10987 65432", "890 Tallur,Kundapur, Karnataka"));
        doctors.add(new Doctor("Dr. Shweta Rao", "MD, DCH", "+91 98765 43210", "123 Kankanady, Mangalore, Dakshina Kannada"));
        doctors.add(new Doctor("Dr. Prakash Pai", "MD, DNB", "+91 87654 32109", "456 Manipal, Udupi, Udupi"));
        doctors.add(new Doctor("Dr. Manoj Shenoy", "MD, MRCPCH", "+91 21098 76543", "567 Manipal, Udupi, Udupi"));
        doctors.add(new Doctor("Dr. Meera Menon", "MBBS, DCH", "+91 76543 21098", "789 Kadri, Mangalore, Dakshina Kannada"));
        doctors.add(new Doctor("Dr. Nandini Sharma", "MBBS, DCH", "+91 54321 09876", "678 Balmatta, Mangalore, Dakshina Kannada"));
        doctors.add(new Doctor("Dr. Sanjay Bhat", "MD, DNB", "+91 43210 98765", "901 Karkala, Udupi, Udupi"));
        doctors.add(new Doctor("Dr. Sneha Kamath", "MBBS, DCH", "+91 32109 87654", "234 Surathkal, Mangalore, Dakshina Kannada"));
        doctors.add(new Doctor("Dr. Anjali Bhandary", "MBBS, DNB", "+91 10987 65432", "890 Moodbidri, Mangalore, Dakshina Kannada"));

        return doctors;
    }
}
