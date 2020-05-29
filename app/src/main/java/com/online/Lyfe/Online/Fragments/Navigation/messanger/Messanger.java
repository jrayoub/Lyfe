package com.online.Lyfe.Online.Fragments.Navigation.messanger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.online.Lyfe.R;


public class Messanger extends Fragment {
    private View view;
    ImageButton SMB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.messanger, container, false);
        initialize();
        SMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getActivity() != null;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, new send_message(), "send_message")
                        .addToBackStack("send_message").commit();
            }
        });
        return view;
    }

    private void initialize() {
        SMB = view.findViewById(R.id.SMB);
    }


}
