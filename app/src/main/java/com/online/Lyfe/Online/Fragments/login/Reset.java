package com.online.Lyfe.Online.Fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.online.Lyfe.R;

public class Reset extends Fragment implements View.OnClickListener {
    private View view;
    private EditText email;
    private LinearLayout send, message;
    private String Semail;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reset, container, false);
        insialize();
        setVisibilty(false);
        seOnclickManagment();
        return view;

    }

    private void seOnclickManagment() {
        send.setOnClickListener(this);
    }

    private void insialize() {
        auth = FirebaseAuth.getInstance();
        message = view.findViewById(R.id.message);
        email = view.findViewById(R.id.email);
        send = view.findViewById(R.id.send);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == send.getId()) {
            reset();
        }
    }

    private void reset() {
        getdata();
        if (Semail.isEmpty()) {
            Toast.makeText(getActivity(), "please enter your email", Toast.LENGTH_SHORT).show();
        } else {
            auth.sendPasswordResetEmail(Semail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        setVisibilty(true);
                    } else {
                        Toast.makeText(getActivity(), "make sure that your email is registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setVisibilty(boolean b) {
        if (b)
            message.setVisibility(View.VISIBLE);
        else
            message.setVisibility(View.GONE);

    }


    private void getdata() {
        Semail = email.getText().toString();
    }
}
