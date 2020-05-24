package com.online.Lyfe.Online.Fragments.Navigation;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.online.Lyfe.Online.Adapter.frieds_request;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;

import java.util.ArrayList;


public class Friend extends Fragment implements frieds_request.friend_holder.onclick {
    private View view;
    private ArrayList<user_list> users;
    private DatabaseReference user;
    private frieds_request adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.friends, container, false);
        initialize();
        getUserData();
        set_up_adapter();
        return view;
    }

    private void set_up_adapter() {
        adapter = new frieds_request(users, this);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);


    }

    private void getUserData() {
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    users.add(item.getValue(user_list.class));
                }
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }, 2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialize() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        progressBar = view.findViewById(R.id.progres);
        progressBar.setVisibility(View.VISIBLE);
        user = db.getReference().child("Users");
        users = new ArrayList<>();
    }

    @Override
    public void delete(int position) {
        removeItem(position);
    }

    private void removeItem(int adapterPosition) {
        users.remove(adapterPosition);
        adapter.notifyDataSetChanged();
    }

}
