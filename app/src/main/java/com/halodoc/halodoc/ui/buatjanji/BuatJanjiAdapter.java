package com.halodoc.halodoc.ui.buatjanji;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halodoc.halodoc.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BuatJanjiAdapter extends RecyclerView.Adapter<BuatJanjiAdapter.BuatJanjiViewHolder> {

    private ArrayList<BuatJanjiModel> listBuatJanji = new ArrayList<>();
    public void setData(ArrayList<BuatJanjiModel> items) {
        listBuatJanji.clear();
        listBuatJanji.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public BuatJanjiViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_buatjanji, parent, false);
        return new BuatJanjiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BuatJanjiViewHolder holder, int position) {
        holder.bind(listBuatJanji.get(position));
    }

    @Override
    public int getItemCount() {
        return listBuatJanji.size();
    }

    public static class BuatJanjiViewHolder extends RecyclerView.ViewHolder {

        CardView cvHistoryBuatJanji;
        ImageView dp;
        TextView name, type, location, notes, bookingDate, status;
        View background;

        public BuatJanjiViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cvHistoryBuatJanji = itemView.findViewById(R.id.cvHistoryBuatJanji);
            dp = itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            location = itemView.findViewById(R.id.location);
            notes = itemView.findViewById(R.id.note);
            bookingDate = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            background = itemView.findViewById(R.id.background);


        }

        public void bind(BuatJanjiModel buatJanjiModel) {

            name.setText(buatJanjiModel.getName());
            type.setText(buatJanjiModel.getType());
            location.setText(buatJanjiModel.getLocation());
            notes.setText(buatJanjiModel.getNotes());
            bookingDate.setText(buatJanjiModel.getBookingDate());
            status.setText(buatJanjiModel.getStatus());

            Glide.with(itemView.getContext())
                    .load(buatJanjiModel.getDp())
                    .into(dp);

            if(buatJanjiModel.getStatus().equals("Selesai")) {
                background.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.done));
            }

            cvHistoryBuatJanji.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), BuatJanjiDetailActivity.class);
                intent.putExtra(BuatJanjiDetailActivity.EXTRA_HOSPITAL, buatJanjiModel);
                itemView.getContext().startActivity(intent);
            });

        }
    }
}
