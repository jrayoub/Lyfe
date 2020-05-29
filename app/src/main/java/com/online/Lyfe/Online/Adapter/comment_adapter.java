package com.online.Lyfe.Online.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.online.Lyfe.Online.Activities.replay;
import com.online.Lyfe.Online.Model.comment_model;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class comment_adapter extends RecyclerView.Adapter {
    private ArrayList<comment_model> comments;
    private DatabaseReference database;
    String TimeLineKey;
    private FirebaseUser user = FirebaseAuth
            .getInstance().getCurrentUser();
    private Context mcontext;

    public comment_adapter(ArrayList<comment_model> comments, String key) {
        database = FirebaseDatabase.getInstance().getReference().child("Timeline").child(key).child("comments");
        this.TimeLineKey = key;
        this.comments = comments;
    }

    @Override
    public int getItemViewType(int position) {
        if (comments.get(position).getImage() == null || comments.get(position).getImage().equals(""))
            return 0;
        return 1;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
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
        final String key = comments.get(position).getKey();
        if (comments.get(position).getImage() == null || comments.get(position).getImage().equals("")) {
            final commnet_holder holder1 = (commnet_holder) holder;
            holder1.name.setText(comments.get(position).getName());
            holder1.comment.setText(comments.get(position).getComment());
            Picasso.get().load(comments.get(position).getPic()).into(holder1.pic);
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
            holder1.replay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setkey = new Intent(mcontext, replay.class);
                    setkey.putExtra("key", TimeLineKey);
                    setkey.putExtra("secondKey", comments.get(position).getKey());
                    mcontext.startActivity(setkey);
                }
            });
        } else {
            commnet_holder_image holder_image = (commnet_holder_image) holder;
            holder_image.name.setText(comments.get(position).getName());
            holder_image.comment.setText(comments.get(position).getComment());
            Picasso.get().load(comments.get(position).getPic()).into(holder_image.pic);
            Picasso.get().load(comments.get(position).getImage()).into(holder_image.image);
        }
    }

    private void like(String key) {
        DatabaseReference like = database.child(key).child("likes").child(user.getUid());
        like.setValue(user.getUid());
    }

    private void getlikes(String key, final MyCallback myCallback) {
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
        return comments.size();
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
