package com.udl.viladegut.ortega.reversi.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udl.viladegut.ortega.reversi.R;

public class LogFragment extends Fragment {

    private static final String PARCEL_FRAG_LOG = "log_frag";
    TextView textView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            textView.setText(savedInstanceState.getString(PARCEL_FRAG_LOG));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        textView = (TextView) view.findViewById(R.id.textLog);
        return view;
    }

    public void putLog(String text) {
        String textLog;
        textLog = textView.getText().toString() + "\n" + text;
        textView.setText(textLog);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PARCEL_FRAG_LOG, textView.getText().toString());
    }


}
