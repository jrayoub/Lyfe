package com.online.Lyfe.Online.Fragments.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends Fragment {
    private View view;
    CircleImageView profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        inisialize();
        return view;
    }

    private void inisialize() {

        profile = view.findViewById(R.id.profile);

        Picasso.get().load("https://data.whicdn.com/images/307953035/original.jpg")
                .into(profile);
    }
}
