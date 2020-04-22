package com.online.Lyfe.Online.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Model.freindlist;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class frieds_request extends RecyclerView.Adapter<frieds_request.freindholder> {
    private ArrayList<freindlist> freind_list;
    private Context mcontext;
    private freindholder.onaddclick click;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public frieds_request(ArrayList<com.online.Lyfe.Online.Model.freindlist> freindlist, Context mcontext, freindholder.onaddclick click) {
        this.freind_list = freindlist;
        this.mcontext = mcontext;
        this.click = click;
    }

    @NonNull
    @Override
    public freindholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freinditem, parent, false);
        return new freindholder(view, click);
    }

    @Override
    public void onBindViewHolder(@NonNull final freindholder holder, int position) {
        final freindlist thisUser = new freindlist(freind_list.get(position).getFullnaame(),
                freind_list.get(position).getEmail(),
                freind_list.get(position).getId());
        //checking the following
        isFollowing(thisUser.getId(), thisUser, holder.add, holder.container);
        //loading data
        holder.name.setText(freind_list.get(position).getFullnaame());
        Picasso.get().load("https://data.whicdn.com/images/307953035/original.jpg")
                .into(holder.profile);
        // on add
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.add.getText().toString().equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                            .child("following").child(thisUser.getId()).setValue(thisUser);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(thisUser.getId())
                            .child("followers").child(user.getUid()).removeValue();
                    addNotification(thisUser.getId());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                            .child("following").child(thisUser.getId()).removeValue();
                   /* FirebaseDatabase.getInstance().getReference().child("Follow").child(thisUser.getId())
                            .child("followers").child(user.getUid()).removeValue();*/
                }
            }

        });
    }


    @Override
    public int getItemCount() {
        return freind_list.size();
    }

    public static class freindholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        onaddclick click;
        Button add;
        CircleImageView profile;
        TextView name;
        LinearLayout container;

        public freindholder(@NonNull View itemView, onaddclick click) {
            super(itemView);
            this.click = click;
            container = itemView.findViewById(R.id.container);
            add = itemView.findViewById(R.id.add);
            profile = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == add.getId()) {
                click.add(getAdapterPosition());
            }
        }

        public interface onaddclick {
            public void add(int position);
        }
    }

    private void isFollowing(final String userid, final freindlist thisUser, final Button button, final LinearLayout container) {
        Toast.makeText(mcontext, "checking", Toast.LENGTH_SHORT).show();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {
                    button.setText("following");
                    //  container.setVisibility(View.GONE);
                } else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", user.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }

}
