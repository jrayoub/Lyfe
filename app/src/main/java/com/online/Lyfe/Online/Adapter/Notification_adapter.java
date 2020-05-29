package com.online.Lyfe.Online.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Activities.ImageScale;
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.R;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Notification_adapter extends RecyclerView.Adapter<Notification_adapter.Notification_holder> {
    private ArrayList<Notification_model> Notifications;
    private Context mContext;

    public Notification_adapter(ArrayList<Notification_model> notifications) {
        Notifications = notifications;

    }

    @NonNull
    @Override
    public Notification_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new Notification_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_holder holder, final int position) {
        holder.textView.setText(Notifications.get(position).getText());
        holder.name.setText(Notifications.get(position).getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Notifications.get(position).getIspost()) {
                    if (Notifications.get(position).getPostid() != null)
                        like(Notifications.get(position).getPostid());
                }
            }
        });
    }

    private void like(String KEY) {
        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("Timeline").child(KEY);
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blog_model ITEM;
                ITEM = dataSnapshot.getValue(blog_model.class);
                Intent scale = new Intent(mContext, ImageScale.class);
                scale.putExtra("url", Objects.requireNonNull(ITEM).getImage());
                scale.putExtra("key", dataSnapshot.getKey());
                scale.putExtra("name", ITEM.getName());
                scale.putExtra("pic", ITEM.getPic());
                scale.putExtra("text", ITEM.getDec());
                scale.putExtra("date", ITEM.getDate());
                mContext.startActivity(scale);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return Notifications.size();
    }

    static class Notification_holder extends RecyclerView.ViewHolder {
        CircleImageView pic;
        TextView textView, name;

        Notification_holder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            textView = itemView.findViewById(R.id.text);
            name = itemView.findViewById(R.id.name);
        }
    }

}
