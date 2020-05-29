package com.online.Lyfe.Online.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Adapter.MessangerAdapter.ICA;
import com.online.Lyfe.Online.Model.messanger.MSI;
import com.online.Lyfe.Online.Model.messanger.message_frontend;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;

import java.util.ArrayList;
import java.util.Objects;


public class InsideChat extends AppCompatActivity {
    EditText Message;
    private String USER_ID, NAME, My_name;

    private ConstraintLayout container;
    private ProgressBar image_progress;
    private ImageButton cancel, send, camera;
    private ImageView imageView;
    private user_list thisUser;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Messanger");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String PROFILE_URL;
    private RecyclerView recyclerView;
    private ICA adapter;
    private ArrayList<MSI> data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        getProfileData();
        initialize();
        setUpRecyclerView();
        getMessages();
        checkIfExist(new Callback() {
            @Override
            public void onScan(String Mkey, boolean exist) {
                if (exist) {
                    LoadRequestMessages(Mkey);
                }
            }

            private void LoadRequestMessages(String mkey) {
                DatabaseReference chat_request = database.child(USER_ID).child("chat_request").child(mkey).child("chat");
                chat_request.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            data.add(item.getValue(MSI.class));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thisUser != null) {
                    checkIfExist(new Callback() {
                        @Override
                        public void onScan(String mkey, boolean exist) {
                            if (exist) {
                                overLoadMessage(mkey);
                            } else {
                                sendMessage();
                            }
                        }
                    });
                }

            }
        });
    }

    private void getMessages() {
    }

    private void setUpRecyclerView() {
        adapter = new ICA(data);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
              /*  if (newState == RecyclerView.SCROLL_STATE_IDLE) {


                }*/
            }
        });
    }

    private void overLoadMessage(String mkey) {
        String MESSAGE = Message.getText().toString();
        MSI msi = new MSI();
        msi.setImage(thisUser.getProfile());
        msi.setMessage(MESSAGE);
        msi.setUser_id(thisUser.getId());
        DatabaseReference chat_request = database.child(USER_ID).child("chat_request").child(mkey).child("chat");
        chat_request.push().setValue(msi);
    }

    private void checkIfExist(final Callback callback) {
        database.child(USER_ID).child("chat_request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean check = false;
                String KEY = "";
                for (DataSnapshot ITEM : dataSnapshot.getChildren()) {
                    message_frontend message = ITEM.getValue(message_frontend.class);
                    if (Objects.requireNonNull(message).getId().equals(thisUser.getId())) {
                        check = true;
                        KEY = ITEM.getKey();
                    }
                }
                callback.onScan(KEY, check);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private interface Callback {
        void onScan(String Mkey, boolean exist);
    }

    private void getProfileData() {
        DatabaseReference USER = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        USER.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(user_list.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        final String MESSAGE = Message.getText().toString();
        if (!MESSAGE.trim().isEmpty()) {
            pushchat(MESSAGE);
            pushRequest(MESSAGE);


        }
    }

    private void pushchat(final String message) {
        message_frontend item = new message_frontend();
        item.setLastMessage(message);
        item.setId(user.getUid());
        item.setName(NAME);
        item.setMYNAME(thisUser.getFullnaame());
        item.setPic(PROFILE_URL);
        item.setMY_pic(thisUser.getProfile());
        final DatabaseReference chat = database.child(user.getUid()).child("chat");
        chat.push().setValue(item, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                String KEY = databaseReference.getKey();
                MSI msi = new MSI();
                msi.setImage(thisUser.getProfile());
                msi.setMessage(message);
                msi.setUser_id(thisUser.getId());
                assert KEY != null;
                chat.child(KEY).child("chat").push().setValue(msi);
            }
        });
    }

    private void pushRequest(final String MESSAGE) {
        message_frontend item_request = new message_frontend();
        item_request.setLastMessage(MESSAGE);
        item_request.setId(thisUser.getId());
        item_request.setName(thisUser.getFullnaame());
        item_request.setMYNAME(NAME);
        item_request.setPic(thisUser.getProfile());
        item_request.setMY_pic(PROFILE_URL);
        final DatabaseReference chat_request = database.child(USER_ID).child("chat_request");
        chat_request.push().setValue(item_request, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                String KEY = databaseReference.getKey();
                MSI msi = new MSI();
                msi.setImage(thisUser.getProfile());
                msi.setMessage(MESSAGE);
                msi.setUser_id(thisUser.getId());
                assert KEY != null;
                chat_request.child(KEY).child("chat").push().setValue(msi);
            }
        });
    }

    private void initialize() {
        USER_ID = getIntent().getStringExtra("USER_ID");
        PROFILE_URL = getIntent().getStringExtra("PROFILE_URL");
        NAME = getIntent().getStringExtra("NAME");
        Message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        camera = findViewById(R.id.camera);
        image_progress = findViewById(R.id.image_progress);
        cancel = findViewById(R.id.cancel);
        imageView = findViewById(R.id.image_container);
        container = findViewById(R.id.container);
        data = new ArrayList<>();
    }


}
