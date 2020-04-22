package com.online.Lyfe.Online.Fragments.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.online.Lyfe.Online.Adapter.frieds_request;
import com.online.Lyfe.Online.Model.freindlist;
import com.online.Lyfe.R;

import java.util.ArrayList;

public class Friend extends Fragment implements frieds_request.freindholder.onaddclick {
    private View view;
    private ArrayList<freindlist> users;
    private ArrayList<String> friend_lists;
    private CollectionReference user;
    private CollectionReference Freind_Collection;
    private frieds_request adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.friends, container, false);
        initialize();
        getUserData();
        read_friend_list();
        setupadapter();
        return view;
    }

    private void read_friend_list() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    friend_lists.add(item.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupadapter() {
        adapter = new frieds_request(users, getContext(), this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void getUserData() {
        user.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private ArrayList<freindlist> freind_list = new ArrayList<>();

            private void getfreinds() {
                Freind_Collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot item : task.getResult()) {
                                for (String key : friend_lists) {
                                    if (item.toObject(freindlist.class).getId() == key) {
                                        Toast.makeText(getContext(), "you did it ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        freind_list.add(item.toObject(freindlist.class));
                                    }
                                }
                            }
                    }
                });
            }

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                    getfreinds();
                for (QueryDocumentSnapshot item : task.getResult()) {
                    {
                                users.add(item.toObject(freindlist.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    private void initialize() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = db.collection("Users");
        Freind_Collection = db.collection("Users");
        users = new ArrayList<>();
        friend_lists = new ArrayList<>();
    }

    @Override
    public void add(int position) {

    }
}
