package com.online.Lyfe.Online.Fragments.Dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.online.Lyfe.R;

import java.util.Objects;

public class FraggmentDialog extends DialogFragment {
    private View view;
    private LinearLayout gallery, camera;
    private onItemChosen onItemChosen;
    private static final String TAG = "dialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog, container, false);
        initialize();
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemChosen.chose(0);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemChosen.chose(1);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemChosen.chose(0);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        return view;
    }

    private void initialize() {
        gallery = view.findViewById(R.id.gallery);
        camera = view.findViewById(R.id.camera);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onItemChosen = (onItemChosen) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ");
        }
    }

    public interface onItemChosen {
        void chose(int chosen);
    }
}
