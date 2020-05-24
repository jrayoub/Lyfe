package com.online.Lyfe.Online.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Model.replay_model;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class replay_adapter extends RecyclerView.Adapter {
    private ArrayList<replay_model> replays;
    private DatabaseReference database;
    private FirebaseUser user = FirebaseAuth
            .getInstance().getCurrentUser();

    public replay_adapter(ArrayList<replay_model> replays, String key, String second_key) {
        database = FirebaseDatabase.getInstance().getReference().child("Timeline").child(key).child("comments").child(second_key);
        this.replays = replays;
    }

    @Override
    public int getItemViewType(int position) {
        if (replays.get(position).getImage() == null || replays.get(position).getImage().equals(""))
            return 0;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.comment, parent, false);
            return new commnet_holder(view);
        }
        view = layoutInflater.inflate(R.layout.comment_image, parent, false);
        return new commnet_holder_image(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final String key = replays.get(position).getKey();
        if (replays.get(position).getImage() == null || replays.get(position).getImage().equals("")) {
            final commnet_holder holder1 = (commnet_holder) holder;
            holder1.name.setText(replays.get(position).getName());
            holder1.comment.setText(replays.get(position).getReplay());
            Picasso.get().load(replays.get(position).getPic()).into(holder1.pic);
            getlikes(key, new MyCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onCallback(long count) {
                    if (count > 0)
                        holder1.like.setText(count + " like ");
                }
            });
            holder1.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like(key);
                    getlikes(key, new MyCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onCallback(long count) {
                            holder1.like.setText(count + " like ");
                        }
                    });
                }
            });
        } else {
            final commnet_holder_image holder_image = (commnet_holder_image) holder;
            holder_image.name.setText(replays.get(position).getName());
            holder_image.comment.setText(replays.get(position).getReplay());
            Picasso.get().load(replays.get(position).getPic()).into(holder_image.pic);
            Picasso.get().load(replays.get(position).getImage()).into(holder_image.image);
            holder_image.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like(key);
                    getlikes(key, new MyCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onCallback(long count) {
                            holder_image.like.setText(count + " like ");
                        }
                    });
                }
            });
        }
    }

    private void like(String key) {
        DatabaseReference like = database.child(key).child("likes").child(user.getUid());
        like.setValue(user.getUid());
    }

    private void getlikes(String key, final replay_adapter.MyCallback myCallback) {
        if (key != null) {
            database.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    myCallback.onCallback(count);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return replays.size();
    }

    public interface MyCallback {
        void onCallback(long count);
    }

    static class commnet_holder extends RecyclerView.ViewHolder {
        TextView name, comment, like, replay;
        //ImageView image;
        CircleImageView pic;

        commnet_holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            pic = itemView.findViewById(R.id.pic);
            comment = itemView.findViewById(R.id.comment);
            like = itemView.findViewById(R.id.like);
            replay = itemView.findViewById(R.id.replay);
        }
    }

    static class commnet_holder_image extends RecyclerView.ViewHolder {
        TextView name, comment, like, replay;
        ImageView image;
        CircleImageView pic;

        commnet_holder_image(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            like = itemView.findViewById(R.id.like);
            replay = itemView.findViewById(R.id.replay);
            image = itemView.findViewById(R.id.image);
        }
    }
}
