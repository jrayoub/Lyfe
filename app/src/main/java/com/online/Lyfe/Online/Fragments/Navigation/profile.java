package com.online.Lyfe.Online.Fragments.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.online.Lyfe.Online.Adapter.Blogs_adapter;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends Fragment implements View.OnClickListener {
    private View view;
    private TextView number_of_followers, number_of_following;
    private LinearLayout followers, following;
    private ArrayList<user_list> following_list;
    private ArrayList<blog_model> data;
    private Blogs_adapter adapter;
    private ArrayList<user_list> followers_list;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        initialize();
        get_data();
        set_onclick_manager();
        setUp_recyclerView();
        return view;
    }

    private void setUp_recyclerView() {
        get_blogs();
        adapter = new Blogs_adapter(data);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.isFocusable();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void get_blogs() {
        DatabaseReference blog_data = FirebaseDatabase.getInstance().getReference().child("Timeline");
        blog_data.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(item.getValue(blog_model.class)).getId().equals(user.getUid())) {
                        blog_model ITEM = item.getValue(blog_model.class);
                        Objects.requireNonNull(ITEM).setKey(item.getKey());
                        data.add(ITEM);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void set_onclick_manager() {
        followers.setOnClickListener(this);
        following.setOnClickListener(this);
    }

    private void initialize() {
        //findViewById
        CircleImageView profile = view.findViewById(R.id.profile);
        number_of_followers = view.findViewById(R.id.number_of_followers);
        number_of_following = view.findViewById(R.id.number_of_following);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);

        //initialize arrays
        following_list = new ArrayList<>();
        followers_list = new ArrayList<>();
        data = new ArrayList<>();

        //setdata;
        Picasso.get().load("https://data.whicdn.com/images/307953035/original.jpg")
                .into(profile);
    }

    private void get_data() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        DatabaseReference follow = database.child("Follow").child(user.getUid());
        follow.child("following").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    following_list.add(item.getValue(user_list.class));
                }
                number_of_following.setText("" + following_list.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        follow.child("followers").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    followers_list.add(item.getValue(user_list.class));
                }
                number_of_followers.setText("" + followers_list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == following.getId()) {
            Toast.makeText(getContext(), "following clicked", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == followers.getId()) {
            Toast.makeText(getContext(), "followers clicked" + followers_list.get(0), Toast.LENGTH_SHORT).show();
        }
    }


}
