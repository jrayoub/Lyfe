package com.online.Lyfe.Online.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Adapter.replay_adapter;
import com.online.Lyfe.Online.Model.replay_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;

import java.util.ArrayList;
import java.util.Objects;

public class replay extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<replay_model> data;
    private String key, secondKey;
    private replay_adapter adapter;
    private RecyclerView recyclerView;
    private int ITEM_COUNT = 20;
    private EditText comment;
    private ImageButton send;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private user_list my_user;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        initialize();
        setUp_recyclerView();
        setOnclicklistener();
        try {
            getcomments(ITEM_COUNT);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnclicklistener() {
        send.setOnClickListener(this);
    }

    private void setUp_recyclerView() {
        adapter = new replay_adapter(data, key, secondKey);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCurrentItem();
                    if (position == ITEM_COUNT - 2) {
                        ITEM_COUNT += 20;
                        //String id = adapter.getlastItemID();
                        getcomments(ITEM_COUNT);

                    }

                }
            }
        });
    }

    private int getCurrentItem() {
        return ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                .findFirstVisibleItemPosition();
    }

    private void getcomments(int ITEM_COUNT) {
        data.clear();
        databaseReference.limitToFirst(ITEM_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    replay_model ITEM;
                    ITEM = item.getValue(replay_model.class);
                    Objects.requireNonNull(ITEM).setKey(item.getKey());
                    data.add(ITEM);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialize() {
        comment = findViewById(R.id.comment);
        send = findViewById(R.id.send);
        //get path
        Intent getkey = getIntent();
        key = getkey.getStringExtra("key");
        secondKey = getkey.getStringExtra("secondKey");
        //initialize data
        data = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Timeline")
                .child(key).child("comments").child(secondKey).child("replay");
    }

    private void get_profile_data() {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                setComment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == send.getId()) {
            get_profile_data();
        }
    }

    private void setComment() {
        String commentS = comment.getText().toString().trim();
        if (!commentS.isEmpty()) {
            replay_model item = new replay_model();
            item.setReplay(commentS);
            item.setName(my_user.getFullnaame());
            item.setPic(my_user.getProfile());
            databaseReference.push().setValue(item);
            comment.setText("");
            //data.clear();
            getcomments(ITEM_COUNT);
        }
    }
}
