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
import com.online.Lyfe.Online.Model.freindlist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class filtler extends AsyncTask<ArrayList<freindlist>, ArrayList<freindlist>, ArrayList<freindlist>> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<freindlist> list = new ArrayList<>();
    private ArrayList<freindlist> newlist = new ArrayList<>();
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
                                list.add(document.toObject(freindlist.class));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    protected ArrayList<freindlist> doInBackground(ArrayList<freindlist>... arrayLists) {
        if (list.isEmpty()) {
            publishProgress(arrayLists[0]);
            return newlist;
        }
        int i = 0;
        for (freindlist item : arrayLists[0]) {
            if (item != list.get(i)) {
                newlist.add(item);
            }
            i++;
        }
        publishProgress(newlist);
        return newlist;
    }


    @Override
    protected void onPostExecute(ArrayList<freindlist> aVoid) {
        super.onPostExecute(aVoid);
        Friend container = mreference.get();
        if (container != null || !container.getActivity().isFinishing()) {
        }
    }

    @Override
    protected void onProgressUpdate(ArrayList<freindlist>... values) {
        super.onProgressUpdate(values);
        Friend container = mreference.get();
        if (container != null || !container.getActivity().isFinishing()) {
        }
    }
}
