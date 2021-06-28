package com.halodoc.halodoc.ui.home.konsultasi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halodoc.halodoc.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConsultationWithDoctorAdapter extends RecyclerView.Adapter<ConsultationWithDoctorAdapter.ConsultationViewHolder> {

    private final ArrayList<ConsultationWithDoctorModel> doctorList = new ArrayList<>();

    private final String option;
    public ConsultationWithDoctorAdapter(String option) {
        this.option = option;
    }

    public void setData(ArrayList<ConsultationWithDoctorModel> items) {
        doctorList.clear();
        doctorList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ConsultationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_doctor, parent, false);
        return new ConsultationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ConsultationViewHolder holder, int position) {
        holder.bind(doctorList.get(position), option);
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class ConsultationViewHolder extends RecyclerView.ViewHolder {

        CardView cvDoctor;
        ImageView dp, thumb;
        TextView name, sertifikatKeahlian, experience, like, price;
        RelativeLayout rl;


        public ConsultationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cvDoctor = itemView.findViewById(R.id.cvDoctor);
            dp = itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.name);
            sertifikatKeahlian = itemView.findViewById(R.id.sertifikatKeahlian);
            experience = itemView.findViewById(R.id.pengalaman);
            like = itemView.findViewById(R.id.like);
            price = itemView.findViewById(R.id.price);
            rl = itemView.findViewById(R.id.rl);
            thumb = itemView.findViewById(R.id.thumb);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ConsultationWithDoctorModel model, String option) {

            Glide.with(itemView.getContext())
                    .load(model.getDp())
                    .into(dp);

            name.setText(model.getName());
            sertifikatKeahlian.setText(model.getSertifikatKeahlian());
            experience.setText("Bekerja selama " + model.getExperience() + "tahun");
            like.setText(model.getLike() + " Orang");
            price.setText("Biaya: Rp." + model.getPrice());


            if(option.equals("verif")){
                price.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
                like.setVisibility(View.GONE);
                thumb.setVisibility(View.GONE);

                cvDoctor.setOnClickListener(view -> {
                    Intent intent = new Intent(itemView.getContext(), VerifiedDoctorDetailActivity.class);
                    intent.putExtra(VerifiedDoctorDetailActivity.EXTRA_DOCTOR, model);
                    itemView.getContext().startActivity(intent);
                });
            } else {
                cvDoctor.setOnClickListener(view -> {
                    Intent intent = new Intent(itemView.getContext(), ConsultationWithDoctorDetailActivity.class);
                    intent.putExtra(ConsultationWithDoctorDetailActivity.EXTRA_DOCTOR, model);
                    itemView.getContext().startActivity(intent);
                });
            }


        }
    }
}
