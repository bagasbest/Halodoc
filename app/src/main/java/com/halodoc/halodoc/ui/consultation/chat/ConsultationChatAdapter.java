package com.halodoc.halodoc.ui.consultation.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.halodoc.halodoc.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConsultationChatAdapter extends RecyclerView.Adapter<ConsultationChatAdapter.MyHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private final ArrayList<ConsultationChatModel> chatList = new ArrayList<>();
    public void setData(ArrayList<ConsultationChatModel> items) {
        chatList.clear();
        chatList.addAll(items);
        notifyDataSetChanged();
    }

    private final String uid;
    public ConsultationChatAdapter(String uid) {
        this.uid = uid;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        String message = chatList.get(position).getMessage();
        String time = chatList.get(position).getTime();

        holder.message.setText(message);
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed user
        if(chatList.get(position).getUid().equals(uid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }



    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView message, time;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageTv);
            time = itemView.findViewById(R.id.timeTv);
        }
    }
}
