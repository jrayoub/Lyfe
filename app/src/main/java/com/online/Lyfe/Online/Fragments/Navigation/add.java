package com.online.Lyfe.Online.Fragments.Navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static io.opencensus.tags.TagKey.MAX_LENGTH;

public class add extends Fragment implements View.OnClickListener {
    private static final int IMAGE_PICK = 22;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private View view;
    private EditText text_holder;
    private TextView name, date;
    private ImageView image_container;
    private CircleImageView pic;
    private LinearLayout image, camera;

    private Button post;
    private ConstraintLayout container;
    private ProgressBar image_progress;
    private user_list my_user;
    private Uri fileURi;
    private Uri downloadUri;
    private ImageButton cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_layout, container, false);
        initialize();
        get_profile_data();
        set_Onclick_Manager();
        return view;
    }

    private void set_Onclick_Manager() {
        post.setOnClickListener(this);
        image.setOnClickListener(this);
        cancel.setOnClickListener(this);
        camera.setOnClickListener(this);
    }

    private void post_data() {
        String data = text_holder.getText().toString();
        if (!data.isEmpty()) {
            assert user != null;
            DatabaseReference blogs = FirebaseDatabase.getInstance().getReference().child("Timeline");
            blog_model blog = new blog_model();
            blog.setId(user.getUid());
            blog.setName(my_user.getFullnaame());
            blog.setDate(Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE));
            blog.setPic(my_user.getProfile());
            if (downloadUri != null) {
                blog.setImage(downloadUri.toString());
            }
            blog.setDec(data);
            restoreUI();
            blogs.push().setValue(blog);

        }
    }

    private void restoreUI() {
        //manage visibility
        text_holder.setText("");
        image.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    private void set_data() {
        name.setText(my_user.getFullnaame());
        date.setText(Calendar.getInstance().getTime().toString());
        Picasso.get().load(my_user.getProfile()).into(pic);
    }

    private void get_profile_data() {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                set_data();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initialize() {
        my_user = new user_list();
        text_holder = view.findViewById(R.id.Text_holder);
        date = view.findViewById(R.id.date);
        name = view.findViewById(R.id.name);
        cancel = view.findViewById(R.id.cancel);
        image = view.findViewById(R.id.image);
        camera = view.findViewById(R.id.camera);
        post = view.findViewById(R.id.post);
        pic = view.findViewById(R.id.pic);
        image_progress = view.findViewById(R.id.image_progress);
        container = view.findViewById(R.id.container);
        image_container = view.findViewById(R.id.image_container);
        container.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == post.getId()) {
            if (post.isEnabled()) {
                post_data();
            } else {
                Toast.makeText(getContext(), "wait tell complete uploading", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == image.getId()) {
            image.setVisibility(View.GONE);
            camera.setVisibility(View.GONE);

            post.setEnabled(false);
            getImage();
        }
        if (v.getId() == cancel.getId()) {
            image.setVisibility(View.VISIBLE);
            camera.setVisibility(View.VISIBLE);
            downloadUri = Uri.EMPTY;
            post.setEnabled(true);
        }
        if (v.getId() == camera.getId()) {
            Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            camera.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //for handling image3 upload to firebase
    private void getImage() {
        Intent upload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(upload, IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileURi = data.getData();
            image_container.setImageURI(fileURi);
            handel_image(true);
            upload_to_firebase();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            assert data != null;
            fileURi = data.getData();
            handel_image(true);
            Bundle extras = data.getExtras();

            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image_container.setImageBitmap(imageBitmap);

            assert imageBitmap != null;
            upload_to_firebase(imageBitmap);
            //imageView.setImageBitmap(imageBitmap);
        } else {
            image.setVisibility(View.VISIBLE);
            camera.setVisibility(View.VISIBLE);
            post.setEnabled(true);
        }
    }

    private void handel_image(boolean b) {
        container.setVisibility(View.VISIBLE);
        if (b) {
            image_progress.setVisibility(View.VISIBLE);
        } else {
            image_progress.setVisibility(View.GONE);
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
                        post.setEnabled(true);
                    }
                    handel_image(false);
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
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
                        post.setEnabled(true);
                    }
                    handel_image(false);
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
