package com.online.Lyfe.Online.Fragments.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.online.Lyfe.Online.Adapter.Blogs_adapter;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.R;

import java.util.ArrayList;
import java.util.Objects;


public class home extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private Blogs_adapter adapter;
    private ArrayList<blog_model> data;
    private ArrayList<String> friends;
    private int ITEM_COUNT = 10;
    private ProgressBar progressBar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        inisialize();
        getfriends();
        get_blogs(ITEM_COUNT);
        setUp_recyclerView();
        return view;
    }


    private void setUp_recyclerView() {
        adapter = new Blogs_adapter(data);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCurrentItem();
                    // Toast.makeText(getContext(), "ff " + position, Toast.LENGTH_SHORT).show();
                    if (position == ITEM_COUNT) {
                        ITEM_COUNT += 20;
                        //String id = adapter.getlastItemID();
                        get_blogs(ITEM_COUNT);

                    }

                }
            }
        });
    }

    private int getCurrentItem() {
        return ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                .findFirstVisibleItemPosition();
    }

    private void get_blogs(int ITEM_COUNT) {
        data.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Timeline");
        databaseReference
                .orderByChild("date")
                .limitToFirst(ITEM_COUNT)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            blog_model post = item.getValue(blog_model.class);
                            for (String key : friends) {
                                assert post != null;
                                if (post.getId().equals(key)) {
                                    blog_model ITEM = item.getValue(blog_model.class);
                                    Objects.requireNonNull(ITEM).setKey(item.getKey());
                                    data.add(ITEM);
                                    //Toast.makeText(getContext(), "" + ITEM.getKey(), Toast.LENGTH_SHORT).show();
                                }
                            }
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

    private void getfriends() {

        assert user != null;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Follow").child(user.getUid()).child("following");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    friends.add(item.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inisialize() {
        progressBar = view.findViewById(R.id.progres);
        data = new ArrayList<>();
        friends = new ArrayList<>();
    }

}
