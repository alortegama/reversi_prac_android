package com.udl.viladegut.ortega.reversi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;


import com.udl.viladegut.ortega.reversi.BDSQLite.AccesBDActivity;

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
        Button checkMatch = findViewById(R.id.buttonConsulta);

        start.setOnClickListener(this);
        help.setOnClickListener(this);
        exit.setOnClickListener(this);
        checkMatch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.buttonStart:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Bundle b = new Bundle();
                Log log = new Log();

                log.setPlayer(sp.getString(getString(R.string.aliasMenu), "Player1"));
                log.setGrid(Integer.parseInt(sp.getString(getString(R.string.boardMenu), "8")));
                if (sp.getString(getString(R.string.GameDifficult), "normal").equals("normal"))
                    b.putInt(Keys.KEY_DIFFICULT, Keys.NORMAL);
                else
                    b.putInt(Keys.KEY_DIFFICULT, Keys.EASY);

                if (sp.getBoolean(getString(R.string.timeMenu), false))
                    b.putBoolean(Keys.KEY_TIME, true);
                else
                    b.putBoolean(Keys.KEY_TIME, false);

                intent = new Intent(MainActivity.this, GameActivity.class);
                b.putParcelable(Keys.KEY_LOG,log);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.buttonHelp:
                intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonConsulta:
                intent = new Intent(MainActivity.this, AccesBDActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonExit:
                finish();
                System.exit(0);
                break;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenu:
                startActivity(new Intent(this, ConfigActivity.class));
                break;
        }
        return true;
    }
}
