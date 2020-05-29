package com.online.Lyfe.Online.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.online.Lyfe.Online.Adapter.comment_adapter;
import com.online.Lyfe.Online.Fragments.Dialog.FraggmentDialog;
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.Online.Model.comment_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static io.opencensus.tags.TagKey.MAX_LENGTH;

public class Comment extends AppCompatActivity implements View.OnClickListener, FraggmentDialog.onItemChosen {
    private static final int IMAGE_PICK = 22;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayList<comment_model> data;
    private String key;
    private comment_adapter adapter;
    private RecyclerView recyclerView;
    private int ITEM_COUNT = 20;
    private EditText comment;
    private ImageButton send, camera;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private user_list my_user;
    private DatabaseReference databaseReference;
    private Uri fileURi;
    private Uri downloadUri;
    private ConstraintLayout container;
    private ProgressBar image_progress;
    private ImageButton cancel;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
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
        camera.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void setUp_recyclerView() {
        adapter = new comment_adapter(data, key);
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
                    comment_model ITEM;
                    ITEM = item.getValue(comment_model.class);
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

    private void sentNotification(blog_model model, user_list my_user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(model.getId());
        Notification_model notification = new Notification_model();
        assert user != null;
        notification.setUserid(user.getUid());
        notification.setText(" Commented on your post");
        notification.setIspost(true);
        notification.setPostid(model.getKey());
        notification.setUserid(my_user.getId());
        notification.setName(my_user.getFullnaame());
        reference.push().setValue(notification);
    }

    private void getPostId(final user_list my_user) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Timeline").child(key);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blog_model item;
                item = dataSnapshot.getValue(blog_model.class);
                assert item != null;
                assert user != null;
                if (!item.getId().equals(user.getUid()))
                    sentNotification(item, my_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initialize() {
        comment = findViewById(R.id.comment);
        send = findViewById(R.id.send);
        camera = findViewById(R.id.camera);

        image_progress = findViewById(R.id.image_progress);
        cancel = findViewById(R.id.cancel);
        imageView = findViewById(R.id.image_container);
        container = findViewById(R.id.container);

        Intent getkey = getIntent();
        key = getkey.getStringExtra("key");
        //Toast.makeText(this, "" + key, Toast.LENGTH_SHORT).show();
        data = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Timeline").child(key).child("comments");
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

    private void addNotification() {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                getPostId(my_user);
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
            addNotification();
        }
        if (v.getId() == camera.getId()) {
            FraggmentDialog dialog = new FraggmentDialog();
            dialog.show(getSupportFragmentManager(), "tage");
        }
        if (v.getId() == cancel.getId()) {
            container.setVisibility(View.GONE);
            image_progress.setVisibility(View.VISIBLE);
        }
    }

    private void setComment() {
        String commentS = comment.getText().toString().trim();
        if (!commentS.isEmpty()) {
            comment_model item = new comment_model();
            item.setComment(commentS);
            item.setName(my_user.getFullnaame());
            if (downloadUri != null) {
                item.setImage(downloadUri.toString());
            }
            item.setPic(my_user.getProfile());
            assert user != null;
            item.setId(user.getUid());
            databaseReference.push().setValue(item);
            comment.setText("");
            //data.clear();
            getcomments(ITEM_COUNT);
            container.setVisibility(View.GONE);
        }
    }

    private void getImage() {
        Intent upload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(upload, IMAGE_PICK);
    }

    private String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void chose(int chosen) {
        if (chosen == 0) {
            openCamera();
        }
        if (chosen == 1) {
            getImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            container.setVisibility(View.VISIBLE);

            fileURi = data.getData();
            upload_to_firebase();
            imageView.setImageURI(fileURi);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data != null;
            fileURi = data.getData();
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            container.setVisibility(View.VISIBLE);

            assert imageBitmap != null;
            upload_to_firebase(imageBitmap);
            imageView.setImageBitmap(imageBitmap);
        } else {
            container.setVisibility(View.GONE);
        }
    }

    private void upload_to_firebase() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String key = random();
        final StorageReference ref = storageRef.child("images/posts" + fileURi.getLastPathSegment() + key);
        UploadTask uploadTask = ref.putFile(fileURi);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    if (task.isComplete()) {
                        image_progress.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void upload_to_firebase(Bitmap bitmap) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String key = random();
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);
        byte[] data = boas.toByteArray();
        final StorageReference ref = storageRef.child("images/posts" + key);
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    if (task.isComplete()) {
                        image_progress.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
