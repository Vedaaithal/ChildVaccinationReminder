package com.example.childvaccinereminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{
    private List<Doctor> doctorList;

    public DoctorAdapter(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_doctor, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);

        holder.doctorNameTextView.setText(doctor.getName());
        holder.doctorQualificationTextView.setText(doctor.getQualification());
        holder.doctorContactTextView.setText(doctor.getContact());
        holder.doctorAddressTextView.setText(doctor.getAddress());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView doctorNameTextView;
        TextView doctorQualificationTextView;
        TextView doctorContactTextView;
        TextView doctorAddressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            doctorQualificationTextView = itemView.findViewById(R.id.doctorQualificationTextView);
            doctorContactTextView = itemView.findViewById(R.id.doctorContactTextView);
            doctorAddressTextView = itemView.findViewById(R.id.doctorAddressTextView);
        }
    }
}