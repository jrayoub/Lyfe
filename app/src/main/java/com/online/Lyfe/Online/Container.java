package com.online.Lyfe.Online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.online.Lyfe.Online.Activities.ImageScale;
import com.online.Lyfe.Online.Fragments.Navigation.Friend;
import com.online.Lyfe.Online.Fragments.Navigation.home;
import com.online.Lyfe.Online.Fragments.Navigation.Notification;
import com.online.Lyfe.Online.Fragments.Navigation.messanger.Messanger;
import com.online.Lyfe.Online.Fragments.Navigation.profile;
import com.online.Lyfe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Container extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    FrameLayout frameLayout;
    private BottomNavigationView mBtmView;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, new home())
                    .commit();
        }
        if (getIntent().hasExtra("category")) {
            if (Objects.equals(getIntent().getStringExtra("category"), "post")) {
                Intent scale = new Intent(this, ImageScale.class);
                scale.putExtra("url", getIntent().getStringExtra("url"));
                scale.putExtra("key", getIntent().getStringExtra("key"));
                scale.putExtra("name", getIntent().getStringExtra("name"));
                scale.putExtra("pic", getIntent().getStringExtra("pic"));
                scale.putExtra("text", getIntent().getStringExtra("text"));
                scale.putExtra("date", getIntent().getStringExtra("date"));
                startActivity(scale);
            }
        }
        insialize();
        navigation();
        mRequestQue = Volley.newRequestQueue(this);
        if (user != null)
            FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
    }

    private void navigation() {
        mBtmView.setOnNavigationItemSelectedListener(this);
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
        switch (item.getItemId()) {
            case R.id.message:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new Messanger(), "messanger")
                        .addToBackStack("messanger").commit();
                break;
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
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new Messanger(), "messanger")
                        .addToBackStack("Messanger").commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), new profile(), "profile")
                        .addToBackStack("profile").commit();
                break;
        }
        return true;
    }

    public void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + "news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "any title");
            notificationObj.put("body", "any body");

            JSONObject extraData = new JSONObject();
            extraData.put("brandId", "puma");
            extraData.put("category", "Shoes");


            json.put("notification", notificationObj);
            json.put("data", extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AIzaSyAE7KUUU6tLrYp-fvZNOJGiLvsjo-YPeAM");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
