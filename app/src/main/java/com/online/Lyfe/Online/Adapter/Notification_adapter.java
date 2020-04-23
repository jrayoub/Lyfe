package com.online.Lyfe.Online.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Notification_adapter extends RecyclerView.Adapter<Notification_adapter.Notification_holder> {
    ArrayList<Notification_model> Notifications;

    public Notification_adapter(ArrayList<Notification_model> notifications) {
        Notifications = notifications;
    }

    @NonNull
    @Override
    public Notification_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_follow, parent, false);
        return new Notification_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_holder holder, int position) {
        holder.textView.setText(Notifications.get(position).getText());
        if (!Notifications.get(position).getIspost()) {
            holder.follow_back.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Notifications.size();
    }

    static class Notification_holder extends RecyclerView.ViewHolder {
        CircleImageView pic;
        TextView textView;
        Button follow_back;

        Notification_holder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            textView = itemView.findViewById(R.id.text);
            follow_back = itemView.findViewById(R.id.follow_back);
        }
    }

}
