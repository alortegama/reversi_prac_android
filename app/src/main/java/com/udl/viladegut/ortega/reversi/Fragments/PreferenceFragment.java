package com.udl.viladegut.ortega.reversi.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udl.viladegut.ortega.reversi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {


    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
