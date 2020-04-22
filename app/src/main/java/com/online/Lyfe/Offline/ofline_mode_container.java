package com.online.Lyfe.Offline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.online.Lyfe.Offline.Fragment.favorite;
import com.online.Lyfe.Offline.Fragment.offline_home;
import com.online.Lyfe.Offline.Model.fragment;
import com.online.Lyfe.Offline.adapter.viewPager;
import com.online.Lyfe.R;

import java.util.Objects;

public class ofline_mode_container extends AppCompatActivity {
    viewPager adapter;
    TabLayout tabLayout;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofline_mode_container);
        settablayout();
    }
    private void settablayout() {
        tabLayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.view_pager);
        adapter = new viewPager(getSupportFragmentManager());
        adapter.addfragment(new fragment("Home", new offline_home()));
        adapter.addfragment(new fragment("Favorite", new favorite()));
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        setUpIcon();
    }

    private void setUpIcon() {
        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.black);
        CardView cardView = findViewById(R.id.card_view);
        cardView.setBackgroundResource(R.drawable.tablayout_corner);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home_black_24dp).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.favorite).getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }
}
