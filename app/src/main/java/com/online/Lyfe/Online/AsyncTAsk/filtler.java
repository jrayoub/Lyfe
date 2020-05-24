package com.online.Lyfe.Online.AsyncTAsk;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.online.Lyfe.Online.Fragments.Navigation.Friend;
import com.online.Lyfe.Online.Model.user_list;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class filtler extends AsyncTask<ArrayList<user_list>, ArrayList<user_list>, ArrayList<user_list>> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<user_list> list = new ArrayList<>();
    private ArrayList<user_list> newlist = new ArrayList<>();
    private WeakReference<Friend> mreference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public filtler(Friend reference) {
        mreference = new WeakReference<Friend>(reference);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        getfriends();
    }

    private void getfriends() {
        db.collection("users").document(user.getUid()).collection("freinds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(user_list.class));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    protected ArrayList<user_list> doInBackground(ArrayList<user_list>... arrayLists) {
        if (list.isEmpty()) {
            publishProgress(arrayLists[0]);
            return newlist;
        }
        int i = 0;
        for (user_list item : arrayLists[0]) {
            if (item != list.get(i)) {
                newlist.add(item);
            }
            i++;
        }
        publishProgress(newlist);
        return newlist;
    }


    @Override
    protected void onPostExecute(ArrayList<user_list> aVoid) {
        super.onPostExecute(aVoid);
        Friend container = mreference.get();
        if (container != null || !container.getActivity().isFinishing()) {
        }
    }

    @Override
    protected void onProgressUpdate(ArrayList<user_list>... values) {
        super.onProgressUpdate(values);
        Friend container = mreference.get();
        if (container != null || !container.getActivity().isFinishing()) {
        }
    }
}
