package com.online.Lyfe.Offline.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

import com.online.Lyfe.Offline.adapter.Favorite_adapter;
import com.online.Lyfe.Offline.databases.datafave;
import com.online.Lyfe.R;

import java.util.ArrayList;
import java.util.Objects;

public class favorite extends Fragment implements Favorite_adapter.Holder.itemclick {
    private View view;
    private ArrayList Data;
    private datafave data;
    private Favorite_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite, container, false);
        getdata();
        inisialize();
        return view;
    }

    private void getdata() {
        Data = new ArrayList();
        data = new datafave(getActivity());
        Data = data.getfav();
    }

    private void inisialize() {
        adapter = new Favorite_adapter(Data, this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void share(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "" + Data.get(position));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void delet(int position) {
        data.delet(Data.get(position).toString());
        Data = data.getfav();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void copy(int position) {
        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", "" + Data.get(position));
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "copied", Toast.LENGTH_SHORT).show();
    }
}


