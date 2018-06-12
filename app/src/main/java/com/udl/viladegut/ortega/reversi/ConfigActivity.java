package com.udl.viladegut.ortega.reversi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.udl.viladegut.ortega.reversi.Fragments.PreferenceFragment;

public class ConfigActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenceFragment()).commit();

    }
}
