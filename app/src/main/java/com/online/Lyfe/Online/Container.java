package com.online.Lyfe.Online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;


import com.online.Lyfe.Online.Fragments.Navigation.Friend;
import com.online.Lyfe.Online.Fragments.Navigation.Notification;
import com.online.Lyfe.Online.Fragments.Navigation.add;
import com.online.Lyfe.Online.Fragments.Navigation.home;
import com.online.Lyfe.Online.Fragments.Navigation.profile;
import com.online.Lyfe.R;


public class Container extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    //private LinearLayout ofline, singout;
    FrameLayout frameLayout;
    private BottomNavigationView mBtmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, new home(), "home")
                    .addToBackStack("home").commit();
        }
        insialize();
        setOnclickmanager();
        navigation();
    }

    private void navigation() {
        mBtmView.setOnNavigationItemSelectedListener(this);
        mBtmView.getMenu().findItem(R.id.home).setChecked(true);
    }


    private void setOnclickmanager() {
        // ofline.setOnClickListener(this);
        //singout.setOnClickListener(this);
    }

    private void insialize() {
        frameLayout = findViewById(R.id.framlayout);
        mBtmView = findViewById(R.id.navigation_bar);
        // ofline = findViewById(R.id.offline);
        //singout = findViewById(R.id.singout);
    }

    @Override
    public void onClick(View view) {
      /*  if (view.getId() == singout.getId()) {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        if (view.getId() == ofline.getId()) {
            startActivity(new Intent(getApplicationContext(), ofline_mode_container.class));
            finish();
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int mMenuId = item.getItemId();
        for (int i = 0; i < mBtmView.getMenu().size(); i++) {
            MenuItem menuItem = mBtmView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }
        switch (item.getItemId()) {
            case R.id.friends:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new Friend(), "freinds")
                        .addToBackStack("Freinds").commit();
                break;
            case R.id.notification:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new Notification(), "notification")
                        .addToBackStack("notification").commit();
                break;
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new home(), "home")
                        .addToBackStack("home").commit();
                break;
            case R.id.add:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new add(), "add")
                        .addToBackStack("add").commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new profile(), "profile")
                        .addToBackStack("profile").commit();
                break;
        }
        return true;
    }

}
