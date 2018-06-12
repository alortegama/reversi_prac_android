package com.udl.viladegut.ortega.reversi.BDSQLite;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.udl.viladegut.ortega.reversi.R;

public class DetailRegActivity extends FragmentActivity {

    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reg);

        buttonBack = (Button) findViewById(R.id.buttonGoBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AccesBDActivity.class));
                finish();
            }
        });
    }


}
