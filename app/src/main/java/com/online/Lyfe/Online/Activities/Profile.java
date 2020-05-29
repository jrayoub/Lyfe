package com.online.Lyfe.Online.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class Profile extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView number_of_followers, number_of_following;
    private LinearLayout followers, following;
    private ArrayList<user_list> following_list;
    private ArrayList<blog_model> data;
    private Blogs_adapter adapter;
    private CircleImageView pic;
    private boolean CheckIfFriend = false;
    private ArrayList<user_list> followers_list;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private int LOAD = 10;
    private TextView name;
    private String id;
    private Button follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        get_profile_data();
        get_data();
        checkIffriend();
        set_onclick_manager();
        setUp_recyclerView();
    }

    private void checkIffriend() {
        if (id.equals(user.getUid())) {
            follow.setVisibility(View.GONE);
        }
        checkIfInfriendList(new CallBack() {
            @Override
            public void isFriend(boolean check) {
                if (check) {
                    follow.setText("UnFollow");
                } else {
                    follow.setText("follow");
                }
            }

            @Override
            public void getUserInfo(user_list model) {

            }
        });
    }

    private void checkIfInfriendList(final CallBack callBack) {
        final DatabaseReference checker = FirebaseDatabase.getInstance().getReference("Follow").child(user.getUid()).child("followers");
        checker.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CheckIfFriend = dataSnapshot.child(id).exists();
                callBack.isFriend(CheckIfFriend);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface CallBack {
        void isFriend(boolean check);

        void getUserInfo(user_list model);
    }

    private void setUp_recyclerView() {
        get_blogs(LOAD);
        adapter = new Blogs_adapter(data);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.isFocusable();
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(id);
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

    private void get_profile_data(final CallBack callBack) {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(id);
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list my_user = dataSnapshot.getValue(user_list.class);
                callBack.getUserInfo(my_user);
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
                    if (Objects.requireNonNull(item.getValue(blog_model.class)).getId().equals(id)) {
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
        pic.setOnClickListener(this);
    }

    private void initialize() {
        //findViewById
        name = findViewById(R.id.name);
        pic = findViewById(R.id.profile);
        number_of_followers = findViewById(R.id.number_of_followers);
        number_of_following = findViewById(R.id.number_of_following);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        follow = findViewById(R.id.follow);

        //initialize arrays
        following_list = new ArrayList<>();
        followers_list = new ArrayList<>();
        data = new ArrayList<>();
        id = getIntent().getStringExtra("user_id");

    }

    private void get_data() {
        DatabaseReference follow = database.child("Follow").child(id);
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
            Toast.makeText(this, "following clicked", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == followers.getId()) {
            Toast.makeText(this, "followers clicked", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == pic.getId()) {
            Toast.makeText(this, "ff", Toast.LENGTH_SHORT).show();
            get_profile_data(new CallBack() {
                @Override
                public void isFriend(boolean check) {

                }

                @Override
                public void getUserInfo(user_list model) {
                    Intent scale = new Intent(getApplicationContext(), ImageScale.class);
                    scale.putExtra("url", model.getProfile());
                    scale.putExtra("key", model.getId());
                    scale.putExtra("name", model.getFullnaame());
                    scale.putExtra("pic", model.getProfile());
                }
            });

        }
    }
}
