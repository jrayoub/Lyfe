package com.online.Lyfe.Online.Fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.online.Lyfe.Offline.ofline_mode_container;
import com.online.Lyfe.Online.Container;
import com.online.Lyfe.R;

public class Login_page extends Fragment implements View.OnClickListener {
    private LinearLayout offline;
    private View view;
    private LinearLayout login;
    private EditText email, password;
    private TextView singup, reset;
    private FirebaseAuth mAuth;
    private String S_email, S_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.loging_page, container, false);
        inisialize();
        setOnclickManagemt();
        return view;
    }

    private void getdata() {
        S_email = email.getText().toString();
        S_password = password.getText().toString();
    }


    private void inisialize() {
        offline = view.findViewById(R.id.offline);
        reset = view.findViewById(R.id.reset);
        login = view.findViewById(R.id.login);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        singup = view.findViewById(R.id.singup);
        mAuth = FirebaseAuth.getInstance();
    }

    private void setOnclickManagemt() {
        reset.setOnClickListener(this);
        login.setOnClickListener(this);
        singup.setOnClickListener(this);
        offline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == login.getId()) {
            Login();
        }
        if (v.getId() == singup.getId()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Register_container, new Sing_up(), "singup").addToBackStack("singup").commit();
        }
        if (v.getId() == reset.getId()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Register_container, new Reset(), "reset").addToBackStack("reset").commit();
        }
        if (v.getId() == offline.getId()) {
            startActivity(new Intent(getActivity(), ofline_mode_container.class));
            getActivity().finish();
        }

    }

    private void Login() {
        getdata();

        if (S_email.isEmpty() || S_password.isEmpty()) {
            Toast.makeText(getActivity(), "please check you info", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(S_email, S_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkVerefecation();
                    } else {
                        Toast.makeText(getActivity(), "email/password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkVerefecation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                updateUI(mAuth.getCurrentUser());
            } else {
                Toast.makeText(getActivity(), "please verify your email", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(getActivity(), Container.class));
            getActivity().finish();
        }
    }
}
