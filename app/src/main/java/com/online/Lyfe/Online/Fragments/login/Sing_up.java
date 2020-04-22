package com.online.Lyfe.Online.Fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.online.Lyfe.R;

import java.util.HashMap;
import java.util.Map;

public class Sing_up extends Fragment implements View.OnClickListener {
    private View view;
    private ProgressBar progressBar;
    private EditText name, email, password;
    private LinearLayout singup;
    private String Sname, Semail, Spassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sing_up, container, false);
        inisialize();
        setOnclickmanagment();
        return view;
    }

    private void getdata() {
        Sname = name.getText().toString();
        Semail = email.getText().toString();
        Spassword = password.getText().toString();
    }

    private void setOnclickmanagment() {
        singup.setOnClickListener(this);
    }

    private void inisialize() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        singup = view.findViewById(R.id.singup);
        progressBar = view.findViewById(R.id.progres);
        SetVisibility(false);
    }

    private void SetVisibility(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == singup.getId()) {
            Singup();
        }
    }

    private void Singup() {
        getdata();
        if (Sname.isEmpty() || Semail.isEmpty() || Spassword.isEmpty()) {
            Toast.makeText(getActivity(), "please check your info", Toast.LENGTH_SHORT).show();
        } else {
            SetVisibility(true);
            mAuth.createUserWithEmailAndPassword(Semail, Spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userid = user.getUid();
                        DocumentReference doc = firestore.collection("Users").document(userid);
                        Map<String, Object> map_user = new HashMap<>();
                        map_user.put("fullnaame", Sname);
                        map_user.put("email", Semail);
                        map_user.put("id", mAuth.getCurrentUser().getUid().toString());
                        verifyaccount();
                        doc.set(map_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                } else {
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "please try again ", Toast.LENGTH_SHORT).show();
                        SetVisibility(false);
                    }
                }
            });
        }

    }

    private void verifyaccount() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
