package com.example.childvaccinereminder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass dataClass = dataList.get(position);
        holder.childName.setText(dataClass.getcName());
        holder.dob.setText(dataClass.getDob());
        holder.bloodGroup.setText(dataClass.getBloodGrp());
        holder.gender.setText(dataClass.getGender());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClass selectedData = dataClass;
                Intent intent = new Intent(context, schedule.class);
                intent.putExtra("childName", dataClass.getcName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView childName, dob, bloodGroup,gender;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recCard = itemView.findViewById(R.id.recCard);
        childName = itemView.findViewById(R.id.child_name);
        dob = itemView.findViewById(R.id.dob);
        bloodGroup = itemView.findViewById(R.id.bldgrp);
        gender=itemView.findViewById(R.id.gen);
    }
}
