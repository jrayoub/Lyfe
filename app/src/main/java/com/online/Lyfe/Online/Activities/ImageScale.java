package com.online.Lyfe.Online.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class ImageScale extends AppCompatActivity implements View.OnClickListener {
    String URL, KEY, PIC, DATE, NAME;
    ImageView image;
    CircleImageView pic;
    ImageButton favorite, comment, share;
    TextView text, name, date, dec;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private String TEXT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scale);
        initialize();
        Picasso();
        setUI();
        setOnclickManager();
    }

    private void setUI() {
        if (KEY != null) {
            DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("Timeline").child(KEY);
            post.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    blog_model ITEM;
                    ITEM = dataSnapshot.getValue(blog_model.class);
                    assert ITEM != null;
                    long count = dataSnapshot.child("likes").getChildrenCount();
                    if (count > 0) {
                        text.setText(count + " likes");
                    }
                    if (dataSnapshot.child("likes").child(user.getUid()).exists()) {
                        favorite.setBackgroundResource(R.drawable.clicked_favorite);
                        favorite.setImageResource(R.drawable.wite_favorite);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            LinearLayout react_container = findViewById(R.id.react_container);
            react_container.setVisibility(View.GONE);
        }

    }

    private void setOnclickManager() {
        favorite.setOnClickListener(this);
        comment.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    private void Picasso() {
        if (URL != null) {
            Picasso.get().load(URL).resize(500, 500).centerInside().into(image);
        } else {
            image.setVisibility(View.GONE);
        }
        Picasso.get().load(PIC).into(pic);
    }

    private void initialize() {
        Intent get = getIntent();
        URL = get.getStringExtra("url");
        KEY = get.getStringExtra("key");
        PIC = get.getStringExtra("pic");
        NAME = get.getStringExtra("name");
        DATE = get.getStringExtra("date");
        TEXT = get.getStringExtra("text");

        image = findViewById(R.id.image);
        favorite = findViewById(R.id.favorit);
        comment = findViewById(R.id.comment);
        share = findViewById(R.id.share);
        text = findViewById(R.id.likes);
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        pic = findViewById(R.id.pic);
        dec = findViewById(R.id.dec);
        name.setText(NAME);
        date.setText(DATE);
        dec.setText(TEXT);
        database = FirebaseDatabase.getInstance().getReference().child("Timeline");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == comment.getId()) {
            Intent comment = new Intent(this, Comment.class);
            comment.putExtra("key", KEY);
            startActivity(comment);
        }
        if (v.getId() == share.getId()) {
            share();
        }
        if (v.getId() == favorite.getId()) {
            like();
        }
    }

    private void like() {
        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("Timeline").child(KEY);
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blog_model ITEM;
                long count = dataSnapshot.child("likes").getChildrenCount();
                ITEM = dataSnapshot.getValue(blog_model.class);
                if (dataSnapshot.child("likes").child(user.getUid()).exists()) {
                    assert ITEM != null;
                    remove();
                    favorite.setBackgroundResource(R.drawable.button_background_rounded);
                    favorite.setImageResource(R.drawable.empty);
                    text.setText(count - 1 + " likes");
                } else {
                    assert ITEM != null;
                    setLike(ITEM);

                    favorite.setBackgroundResource(R.drawable.clicked_favorite);
                    favorite.setImageResource(R.drawable.wite_favorite);
                    text.setText(count + 1 + " likes");

                }
            }

            private void remove() {
                database.child(KEY).child("likes").child(user.getUid()).removeValue();
            }

            private void setLike(blog_model item) {
                if (!item.getId().equals(user.getUid())) {
                    addNOtification(item);
                }
                DatabaseReference liked_data = database.child(KEY).child("likes").child(user.getUid());
                liked_data.setValue(user.getUid());
            }

            private void addNOtification(final blog_model blog_model) {
                assert user != null;
                final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                my_account.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_list my_user = dataSnapshot.getValue(user_list.class);
                        assert my_user != null;
                        sentNotification(my_user, blog_model);
                    }

                    private void sentNotification(user_list theUser, blog_model model) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(model.getId());
                        Notification_model notification = new Notification_model();
                        notification.setUserid(user.getUid());
                        notification.setText(" liked your post");
                        notification.setIspost(true);
                        notification.setPostid(model.getKey());
                        notification.setUserid(theUser.getId());
                        notification.setName(theUser.getFullnaame());
                        reference.push().setValue(notification);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /***************** share methods*****************/
    private void share() {
        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("Timeline").child(KEY);
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blog_model ITEM;
                ITEM = dataSnapshot.getValue(blog_model.class);
                get_user_data(ITEM);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void get_user_data(final blog_model blog_model) {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                shareItnow(blog_model, my_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void shareItnow(blog_model blog_model, user_list user_list) {
        blog_model.setId(user.getUid());
        blog_model.setOwner_nmae(blog_model.getName());
        blog_model.setOwner_pic(blog_model.getPic());
        blog_model.setOwner_id(blog_model.getId());
        blog_model.setOwnerDate(blog_model.getDate());
        blog_model.setName(user_list.getFullnaame());
        blog_model.setPic(user_list.getProfile());
        blog_model.setId(user_list.getId());
        blog_model.setDate(Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE));

        database.push().setValue(blog_model);
        Toast.makeText(this, "post has been shared into your timeline", Toast.LENGTH_SHORT).show();
    }

}
