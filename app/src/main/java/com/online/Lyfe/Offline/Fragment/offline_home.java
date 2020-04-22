package com.online.Lyfe.Offline.Fragment;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.online.Lyfe.Offline.Activities.ofline_browser;
import com.online.Lyfe.Offline.adapter.Home_adapter;
import com.online.Lyfe.Offline.databases.database;
import com.online.Lyfe.R;

import java.util.ArrayList;

public class offline_home extends Fragment implements Home_adapter.myholder.Onclick {
    View view;
    private ArrayList Data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);
        getdata();
        insialize();
        return view;
    }

    private void insialize() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        if (!Data.isEmpty()) {
            Home_adapter adapter = new Home_adapter(Data, this);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void getdata() {
        Data = new ArrayList();
        database data = new database(getActivity());
        Data = data.gettitle();
    }

    @Override
    public void selected(int position) {
        Intent set = new Intent(getActivity(), ofline_browser.class);
        set.putExtra("id", position);
        startActivity(set);
    }
}
