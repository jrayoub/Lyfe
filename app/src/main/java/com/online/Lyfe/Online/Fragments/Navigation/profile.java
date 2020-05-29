package com.online.Lyfe.Online.Fragments.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.online.Lyfe.Online.Activities.settings;
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
    private Button setting;
    private RecyclerView recyclerView;
    private TextView number_of_followers, number_of_following;
    private LinearLayout followers, following;
    private ArrayList<user_list> following_list;
    private ArrayList<blog_model> data;
    private Blogs_adapter adapter;
    CircleImageView pic;
    private ArrayList<user_list> followers_list;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private int LOAD = 10;
    private TextView name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        initialize();
        get_profile_data();
        get_data();
        set_onclick_manager();
        setUp_recyclerView();

        return view;
    }

    private void setUp_recyclerView() {
        get_blogs(LOAD);
        adapter = new Blogs_adapter(data);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.isFocusable();
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCorrentPosition();
                    if (position == LOAD - 2) {
                        LOAD = LOAD + 10;
                        get_blogs(LOAD);
                    }
                }
            }
        });
    }

    private int getCorrentPosition() {
        return ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                .findFirstVisibleItemPosition();
    }

    private void get_profile_data() {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list my_user = dataSnapshot.getValue(user_list.class);
                Picasso.get().load(Objects.requireNonNull(my_user).getProfile()).into(pic);
                name.setText(Objects.requireNonNull(my_user).getFullnaame());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void get_blogs(int LOAD) {
        DatabaseReference blog_data = FirebaseDatabase.getInstance().getReference().child("Timeline");
        blog_data.limitToFirst(LOAD).addValueEventListener(new ValueEventListener() {
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
        setting.setOnClickListener(this);
    }

    private void initialize() {
        //findViewById
        setting = view.findViewById(R.id.setting);
        name = view.findViewById(R.id.name);
        pic = view.findViewById(R.id.profile);
        number_of_followers = view.findViewById(R.id.number_of_followers);
        number_of_following = view.findViewById(R.id.number_of_following);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);

        //initialize arrays
        following_list = new ArrayList<>();
        followers_list = new ArrayList<>();
        data = new ArrayList<>();


    }

    private void get_data() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        DatabaseReference follow = database.child("Follow").child(user.getUid());
        follow.child("following").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                number_of_following.setText("" + count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        follow.child("followers").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                number_of_followers.setText("" + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == setting.getId()) {
            startActivity(new Intent(getActivity(), settings.class));
        }
    }


}
