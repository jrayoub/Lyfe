package com.online.Lyfe.Offline.Model;
import androidx.fragment.app.Fragment;

public class fragment {
    String title;
    Fragment fragment;

    public fragment(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
