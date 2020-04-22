package com.online.Lyfe.Offline.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.online.Lyfe.Offline.adapter.Browser_adapter;
import com.online.Lyfe.Offline.databases.database;
import com.online.Lyfe.Offline.databases.datafave;
import com.online.Lyfe.R;

import java.util.ArrayList;

public class ofline_browser extends AppCompatActivity implements Browser_adapter.holder.itemclick {
    private Intent get;
    private ArrayList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofline_browser);
        getdata();
        inisialize();
    }

    private void getdata() {
        list = new ArrayList();
        get = getIntent();
        int id = get.getIntExtra("id", 0);
        database data = new database(this);
        list = data.gettext(id);
    }

    private void inisialize() {
        Browser_adapter adapter = new Browser_adapter(list, this, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void share(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "" + list.get(position));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void favorite(int position) {
        datafave fv = new datafave(this);
        fv.insert(list.get(position).toString());
    }

    @Override
    public void copy(int position) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", "" + list.get(position));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "copied", Toast.LENGTH_SHORT).show();
    }
}
