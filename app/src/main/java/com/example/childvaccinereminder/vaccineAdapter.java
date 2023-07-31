package com.example.childvaccinereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class vaccineAdapter extends RecyclerView.Adapter<vaccineAdapter.VaccineVH> {
    ArrayList<Versions> versionsList;
    Context context;

    public vaccineAdapter(ArrayList<Versions> versionsList,Context context) {
        this.context = context;
        this.versionsList=versionsList;
    }

    @NonNull
    @Override
    public vaccineAdapter.VaccineVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row,null,false);
        return new VaccineVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vaccineAdapter.VaccineVH holder, int position) {


        holder.vaccineTxt.setText(versionsList.get(position).vaccine_name);
        holder.descriptionTxt.setText(versionsList.get(position).description);

        holder.vaccineTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(versionsList.get(position).isVisible)
                {
                    holder.descriptionTxt.setVisibility(View.GONE);
                    holder.rl_desc_line.setVisibility(View.GONE);
                    holder.rl_title_line.setVisibility(View.VISIBLE);
                    versionsList.get(position).isVisible=false;

                }
                else {
                    holder.descriptionTxt.setVisibility(View.VISIBLE);
                    holder.rl_desc_line.setVisibility(View.VISIBLE);
                    holder.rl_title_line.setVisibility(View.GONE);
                    versionsList.get(position).isVisible=true;
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return versionsList.size();
    }

    public class VaccineVH extends RecyclerView.ViewHolder {

        TextView vaccineTxt,descriptionTxt;
        LinearLayout linearLayout;
        RelativeLayout rl_title_line;
        RelativeLayout rl_desc_line;


        public VaccineVH(@NonNull View itemView) {
            super(itemView);

            vaccineTxt=itemView.findViewById(R.id.vaccine_name);
            descriptionTxt=itemView.findViewById(R.id.description);
            rl_title_line=itemView.findViewById(R.id.rl_title_line);
            rl_desc_line=itemView.findViewById(R.id.rl_desc_line);


        }
    }
}
