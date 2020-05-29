package com.online.Lyfe.Online.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Activities.Profile;
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class frieds_request extends RecyclerView.Adapter<frieds_request.friend_holder> {

    private Context mContext;
    private ArrayList<user_list> mUsers;

    private FirebaseUser firebaseUser;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    public frieds_request(ArrayList<user_list> friend_list, Context context) {
        this.mUsers = friend_list;
        this.mContext = context;
        mRequestQue = Volley.newRequestQueue(mContext);
    }

    @NonNull
    @Override
    public friend_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freinditem, parent, false);
        return new friend_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final friend_holder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final user_list user = mUsers.get(position);
        holder.add.setVisibility(View.VISIBLE);
        isFollowing(user.getId(), holder.add, user);

        holder.name.setText(user.getFullnaame());
        Picasso.get().load(user.getProfile()).into(holder.profile);
        if (user.getId().equals(firebaseUser.getUid())) {
            holder.itemView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Profile.class);
                intent.putExtra("user_id", user.getId());
                mContext.startActivity(intent);

            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.add.getText().toString().equals("follow")) {
                    getuserData(new Callback() {
                        @Override
                        public void onUserLoaded(user_list MY_USER) {
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                    .child("following").child(user.getId()).setValue(user);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                    .child("followers").child(firebaseUser.getUid()).setValue(MY_USER);
                        }
                    });
                    get_profile_data(user.getId(), user);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }

            private void getuserData(final Callback callback) {
                DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                my_account.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_list my_user = dataSnapshot.getValue(user_list.class);
                        assert my_user != null;
                        callback.onUserLoaded(my_user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        });
    }

    public interface Callback {
        void onUserLoaded(user_list MY_USER);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class friend_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button add;
        CircleImageView profile;
        TextView name;
        LinearLayout container;

        friend_holder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            add = itemView.findViewById(R.id.add);
            profile = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


    }

    private void isFollowing(final String userid, final Button button, final user_list CURRENT_USER) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {
                    button.setText("following");
                    //  RemoveItem(CURRENT_USER);
                } else {
                    button.setText("follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String user_id, user_list my_user, user_list user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(user_id);
        String id = firebaseUser.getUid();
        Notification_model hashMap = new Notification_model();
        hashMap.setName(my_user.getFullnaame());
        hashMap.setIspost(false);
        hashMap.setPostid(null);
        hashMap.setText("Started following you");
        hashMap.setUserid(id);
        reference.push().setValue(hashMap);
        sendNotification(user);
    }

    private void sendNotification(user_list user) {
        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + user.getId());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "Lyfe");
            notificationObj.put("body", user.getFullnaame() + "start following you ");

            JSONObject extraData = new JSONObject();
            extraData.put("category", "follow");
            extraData.put("user_id", firebaseUser.getUid());

            json.put("notification", notificationObj);
            json.put("data", extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAABNgTKmw:APA91bF5FSGfZmn5KVK5EpxIrXIwMcZXWTeF6h22WAOdCGdWv2lQbdr5NO5CgW14Xd3qJS_w_JOItJsrS0_XhsvHkBuyRxR0TqcFl0RNaK3OjQKmBm1oRwNMQMnzim8rwxsYMWVRNRrF");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void get_profile_data(final String user_id, final user_list user) {
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                addNotification(user_id, my_user, user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
