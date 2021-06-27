package com.halodoc.halodoc.ui.home.buatjanji;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.halodoc.halodoc.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.BuatJanjiRSViewHolder> {


    private final ArrayList<HospitalModel> buatJanjiList = new ArrayList<>();
    public void setData(ArrayList<HospitalModel> items) {
        buatJanjiList.clear();
        buatJanjiList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public BuatJanjiRSViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buatjanjirs, parent, false);
        return new BuatJanjiRSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BuatJanjiRSViewHolder holder, int position) {
        holder.bind(buatJanjiList.get(position));
    }

    @Override
    public int getItemCount() {
        return buatJanjiList.size();
    }


    public static class BuatJanjiRSViewHolder extends RecyclerView.ViewHolder {

        ImageView dp;
        TextView name, type, location;
        CardView cv;

        public BuatJanjiRSViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.hospitalDp);
            name = itemView.findViewById(R.id.hospitalName);
            type = itemView.findViewById(R.id.hospitalType);
            location = itemView.findViewById(R.id.hospotalLocation);
            cv = itemView.findViewById(R.id.cvBuatJanji);
        }

        public void bind(HospitalModel hospitalModel) {
            name.setText(hospitalModel.getName());
            type.setText(hospitalModel.getType());
            location.setText(hospitalModel.getLocation());

            Glide.with(itemView.getContext())
                    .load(hospitalModel.getDp())
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(dp);

            // MENNUJU DETAIL RUMAH SAKIT
            cv.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), HospitalDetailActivity.class);
                intent.putExtra(HospitalDetailActivity.EXTRA_HOSPITAL, hospitalModel);
                itemView.getContext().startActivity(intent);
            });

        }
    }
}
