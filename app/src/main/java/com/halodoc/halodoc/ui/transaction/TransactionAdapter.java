package com.halodoc.halodoc.ui.transaction;

import android.annotation.SuppressLint;
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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final ArrayList<TransactionModel> listTransaction = new ArrayList<>();

    private final String role;
    public TransactionAdapter(String role) {
        this.role = role;
    }

    public void setData(ArrayList<TransactionModel> items) {
        listTransaction.clear();
        listTransaction.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listTransaction.get(position), role);
    }

    @Override
    public int getItemCount() {
        return listTransaction.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        View bg;
        ImageView dp;
        TextView name, type, notes, time, price, status;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cvTransaction);
            bg = itemView.findViewById(R.id.background);
            dp = itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            notes = itemView.findViewById(R.id.notes);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);

        }

        @SuppressLint("SetTextI18n")
        public void bind(TransactionModel model, String role) {

            Glide.with(itemView.getContext())
                    .load(model.getDp())
                    .into(dp);

            name.setText(model.getName());
            type.setText(model.getServices());
            notes.setText(model.getNotes());
            time.setText(model.getBookingDate());
            price.setText("Rp. " + model.getPrice());
            status.setText(model.getStatus());

            if(model.getStatus().equals("Sudah Diverifikasi")) {
                bg.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.done));

            }

            cv.setOnClickListener(view -> {
                if(role.equals("admin")) {
                    Intent intent = new Intent(itemView.getContext(), TransactionDetailActivity.class);
                    intent.putExtra(TransactionDetailActivity.EXTRA_TRANSACTION, model);
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }
}
