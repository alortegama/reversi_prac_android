package com.udl.viladegut.ortega.reversi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView1);
        imageView.setImageResource(R.drawable.reversi_titulo);

        Button start = findViewById(R.id.buttonStart);
        Button help = findViewById(R.id.buttonHelp);
        Button exit = findViewById(R.id.buttonExit);

        start.setOnClickListener(this);
        help.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.buttonStart:
                intent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonHelp:
                intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonExit:
                finish();
                System.exit(0);
                break;
        }
    }
}
