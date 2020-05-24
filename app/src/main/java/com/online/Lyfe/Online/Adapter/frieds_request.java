package com.online.Lyfe.Online.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class frieds_request extends RecyclerView.Adapter<frieds_request.friend_holder> {

    private ArrayList<user_list> freind_list;


    private DatabaseReference follow
            = FirebaseDatabase.getInstance().getReference().child("Follow");

    private FirebaseUser user
            = FirebaseAuth.getInstance().getCurrentUser();

    private user_list me;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private friend_holder.onclick monclick;
    private user_list my_user = new user_list();

    public frieds_request(ArrayList<user_list> friend_list, friend_holder.onclick onclick) {
        this.freind_list = friend_list;
        this.monclick = onclick;
    }

    @NonNull
    @Override
    public friend_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freinditem, parent, false);
        return new friend_holder(view, monclick);
    }

    @Override
    public void onBindViewHolder(@NonNull final friend_holder holder, final int position) {

        final user_list thisUser = freind_list.get(position);

        //checking the following
        isFollowing(thisUser.getId(), holder, position);

        //loading data
        holder.name.setText(freind_list.get(position).getFullnaame());
        Picasso.get().load("https://data.whicdn.com/images/307953035/original.jpg")
                .into(holder.profile);

        if (thisUser.getId().equals(firebaseUser.getUid())) {
            me = thisUser;
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.add.getText().toString().equals("follow")) {

                    //set following
                    follow.child(user.getUid()).child("following").child(thisUser.getId()).setValue(thisUser);

                    //set followers
                    follow.child(freind_list.get(position).getId()).child("followers").child(user.getUid()).setValue(me);
                    //send notification
                    get_profile_data(thisUser.getId());
                    //addNotification(thisUser.getId());

                } else {
                    follow.child(user.getUid()).child("following").child(thisUser.getId()).removeValue();
                    follow.child(thisUser.getId()).child("followers").child(user.getUid()).removeValue();
                }
            }

        });
    }


    @Override
    public int getItemCount() {
        return freind_list.size();
    }

    public static class friend_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button add;
        CircleImageView profile;
        TextView name;
        LinearLayout container;
        onclick onclik;

        friend_holder(@NonNull View itemView, onclick onclick) {
            super(itemView);
            this.onclik = onclick;
            container = itemView.findViewById(R.id.container);
            add = itemView.findViewById(R.id.add);
            profile = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public interface onclick {
            void delete(int position);
        }
    }

    private void isFollowing(final String userid, final friend_holder friend_holder, final int position) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        DatabaseReference reference = follow.child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {
                    friend_holder.add.setText("following");
                    try {
                        friend_holder.onclik.delete(position);
                    } catch (Exception ignored) {
                    }
                    notifyDataSetChanged();
                } else {
                    friend_holder.add.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String user_id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user_id);
        String id = user.getUid();
        Notification_model hashMap = new Notification_model();
        assert my_user != null;
        hashMap.setName(my_user.getFullnaame());
        hashMap.setIspost(false);
        hashMap.setPostid(null);
        hashMap.setText("Started following you");
        hashMap.setUserid(id);
        reference.push().setValue(hashMap);
    }

    private void get_profile_data(final String user_id) {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_user = dataSnapshot.getValue(user_list.class);
                addNotification(user_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}


