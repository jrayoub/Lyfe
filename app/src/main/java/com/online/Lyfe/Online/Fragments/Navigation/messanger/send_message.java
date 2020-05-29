package com.online.Lyfe.Online.Fragments.Navigation.messanger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.online.Lyfe.Online.Adapter.MessangerAdapter.SMA;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;

import java.util.ArrayList;
import java.util.Objects;


public class send_message extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SMA adapter;
    private ArrayList<user_list> USERS;
    private int ITEM_COUNT = 10;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.messanger, container, false);
        initialize();
        getFollowers(ITEM_COUNT);
        setUpRecyclerVIew();
        return view;
    }

    private void initialize() {
        ImageButton SMB = view.findViewById(R.id.SMB);
        SMB.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progres);
        USERS = new ArrayList<>();
    }

    private void setUpRecyclerVIew() {
        adapter = new SMA(USERS);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCurrentItem();
                    if (position == ITEM_COUNT) {
                        ITEM_COUNT += 20;
                        getFollowers(ITEM_COUNT);

                    }

                }
            }
        });
    }

    private void getFollowers(int item_count) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid()).child("following");
        databaseReference
                .limitToFirst(item_count)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            user_list USER = item.getValue(user_list.class);
                            USERS.add(USER);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

    }

    private int getCurrentItem() {
        return ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                .findFirstVisibleItemPosition();
    }

}
