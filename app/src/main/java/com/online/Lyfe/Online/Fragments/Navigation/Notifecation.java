package com.online.Lyfe.Online.Fragments.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.online.Lyfe.Online.Adapter.Notification_adapter;
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.R;

import java.util.ArrayList;

public class Notifecation extends Fragment {
    private View view;
    private ArrayList<Notification_model> notifications;
    private Notification_adapter adapter;
    private DatabaseReference notification_reference;
    private FirebaseUser user;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification, container, false);
        inisialize();
        get_Notifications();
        set_up_adapter();
        return view;
    }

    private void get_Notifications() {
        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    notifications.add(item.getValue(Notification_model.class));
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void set_up_adapter() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new Notification_adapter(notifications);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void inisialize() {
        progressBar = view.findViewById(R.id.progres);
        user = FirebaseAuth.getInstance().getCurrentUser();
        notification_reference = FirebaseDatabase.getInstance().getReference().child("Notifications").child(user.getUid());
        notifications = new ArrayList<>();

    }
}
