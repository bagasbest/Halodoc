package com.halodoc.halodoc.ui.consultation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.ui.consultation.chat.ConsultationChatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConsultationAdapter extends RecyclerView.Adapter <ConsultationAdapter.ViewHolder> {

    private ArrayList<ConsultationModel> consultationList = new ArrayList<>();
    public void setData (ArrayList<ConsultationModel> items) {
        consultationList.clear();
        consultationList.addAll(items);
        notifyDataSetChanged();
    }

    String currentUid;
    public ConsultationAdapter(String currentUid) {
        this.currentUid = currentUid;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(consultationList.get(position), currentUid);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView dp;
        View bg;
        TextView name, type, price, status;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cvConsultation);
            dp = itemView.findViewById(R.id.dp);
            bg = itemView.findViewById(R.id.background);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
        }

        public void bind(ConsultationModel model, String currentUid) {

            if(model.getUserUid().equals(currentUid)) {
                Glide.with(itemView.getContext())
                        .load(model.getDoctorDp())
                        .into(dp);
                name.setText(model.getDoctorName());
                type.setText(model.getService());
            }
            else  {
                Glide.with(itemView.getContext())
                        .load(model.getUserDp())
                        .into(dp);
                name.setText(model.getUserName());
                type.setVisibility(View.GONE);
            }
            price.setText(model.getPrice());
            status.setText(model.getStatus());

            if(model.getStatus().equals("Selesai")) {
                bg.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.done));
            }


            cv.setOnClickListener(view -> {
                if(model.getStatus().equals("waiting")) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Pembayaran Sedang Diverifikasi")
                            .setMessage("Kamu akan langsung dapat berkonsultasi setelah admin Halodoc memverifikasi bukti pembayaran kamu")
                            .setIcon(R.drawable.ic_baseline_warning_amber_24)
                            .setCancelable(false)
                            .setPositiveButton("YA", (dialogInterface, i) -> {
                               dialogInterface.dismiss();
                            })
                            .show();
                } else if(model.getStatus().equals("Sedang Dalam Proses")) {
                    Intent intent = new Intent(itemView.getContext(), ConsultationChatActivity.class);
                    intent.putExtra(ConsultationChatActivity.EXTRA_CONSULTATION, model);
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }
}
