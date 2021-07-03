package com.halodoc.halodoc.ui.home.tokokesehatan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halodoc.halodoc.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TokoKesehatanAdapter extends RecyclerView.Adapter<TokoKesehatanAdapter.ViewHolder> {

    private final ArrayList<TokoKesehatanModel> listProduct = new ArrayList<>();
    public void setData(ArrayList<TokoKesehatanModel> items){
        listProduct.clear();
        listProduct.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public TokoKesehatanAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TokoKesehatanAdapter.ViewHolder holder, int position) {
        holder.bind(listProduct.get(position));
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout clItem;
        ImageView dp;
        TextView name, price, description;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            clItem = itemView.findViewById(R.id.itemProduct);
            dp = itemView.findViewById(R.id.roundedImageView);
            name = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
        }

        @SuppressLint("SetTextI18n")
        public void bind(TokoKesehatanModel model) {

            Glide.with(itemView.getContext())
                    .load(model.getDp())
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(dp);

            name.setText(model.getName());
            price.setText("Rp" + model.getPrice());
            description.setText(model.getDescription());

            clItem.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), TokoKesehatanDetailActivity.class);
                intent.putExtra(TokoKesehatanDetailActivity.EXTRA_MARKET, model);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
