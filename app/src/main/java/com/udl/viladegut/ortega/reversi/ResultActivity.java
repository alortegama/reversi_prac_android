package com.udl.viladegut.ortega.reversi;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udl.viladegut.ortega.reversi.BDSQLite.LogSQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText2;
    TextView tvStateMatch;
    Boolean time;
    Log log;

    public static final String ALIAS_BD = "alias";
    public static final String DATE_TIME_BD = "data_hora";
    public static final String TABLE_BD = "tabla";
    public static final String NUM_NEGRAS_BD = "num_negras";
    public static final String NUM_BLANCAS_BD = "num_blancas";
    public static final String TOTAL_TIME_BD = "tiempo_total";
    public static final String RESULTAT_BD = "resultado";

    private LogSQLiteHelper LogHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            time = bundle.getBoolean(Keys.KEY_TIME);
            log = bundle.getParcelable(Keys.KEY_LOG);

            EditText editText1 = findViewById(R.id.actualDateTime);
            editText1.setText(log.getDate_time());

            editText2 = findViewById(R.id.logValues);
            editText2.setText(obtainLog());

            tvStateMatch = findViewById(R.id.stateResult);
            tvStateMatch.setText(log.getResult());

            Button button1 = findViewById(R.id.buttonSendEmail);
            button1.setOnClickListener(this);

            Button button2 = findViewById(R.id.buttonGoBack);
            button2.setOnClickListener(this);

            Button button3 = findViewById(R.id.buttonExit);
            button3.setOnClickListener(this);
        } else
            Toast.makeText(this, "Error al mostrar els resultats", Toast.LENGTH_LONG).show();

        //Accedim a la BBDD per guardar el resultat
        LogHelper = new LogSQLiteHelper(this, "DBReversi", null, 2);
        db = LogHelper.getWritableDatabase();
        create();
    }

    //Afegim les dades de la partida a la BBDD
    private void create() {
        if (db != null) {
            ContentValues newRegister = new ContentValues();

            newRegister.put(ALIAS_BD, log.getPlayer());
            newRegister.put(DATE_TIME_BD, log.getDate_time());
            newRegister.put(TABLE_BD, log.getGrid());
            newRegister.put(NUM_NEGRAS_BD, log.getNum_pieces_player());
            newRegister.put(NUM_BLANCAS_BD, log.getNum_pieces_cpu());
            if (time)
                newRegister.put(TOTAL_TIME_BD, log.getTime_remaining());
            newRegister.put(RESULTAT_BD, log.getResult());

            db.insert("Logs", null, newRegister);
        } else
            Toast.makeText(this, "No s'ha pogut guardar a la BD", Toast.LENGTH_LONG).show();
    }

    private String obtainLog() {
        int dif = (log.getNum_pieces_player() > log.getNum_pieces_cpu()) ?
                log.getNum_pieces_player() - log.getNum_pieces_cpu() :
                log.getNum_pieces_cpu() - log.getNum_pieces_player();


        String player = getString(R.string.alias) + " " + log.getPlayer();
        String result = log.getResult();

        String timeLeft = "";
        String graella = getString(R.string.grid_size).concat(" ") + log.getGrid();
        String fichasJugador = log.getPlayer().concat(" : ") + log.getNum_pieces_player() + " ".concat(getString(R.string.game_piece));
        String fichasCPU = getString(R.string.cpu_name).concat(" : ") + log.getNum_pieces_cpu() + " ".concat(getString(R.string.game_piece));
        String diference = dif + " ".concat(getString(R.string.grid_diference));

        if (time)
            timeLeft = getString(R.string.left_text) + " " + log.getTime_remaining() + " ".concat(getString(R.string.seconds_long));


        return player.concat("\n")
                .concat(result).concat("\n")
                .concat(graella).concat("\n")
                .concat(fichasJugador).concat("\n")
                .concat(fichasCPU).concat("\n")
                .concat(diference).concat("\n")
                .concat(timeLeft);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonGoBack:
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finish();
                /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

                intent = new Intent(ResultActivity.this, GameActivity.class);
                b.putParcelable(Keys.KEY_LOG,log);
                intent.putExtras(b);
                startActivity(intent);
                finish();*/
                break;
            case R.id.buttonExit:
                finishAffinity();
                break;
            case R.id.buttonSendEmail:
                EditText editText3 = findViewById(R.id.addressEmail);

                if (editText3.getText().toString().isEmpty())
                    Toast.makeText(v.getContext(), R.string.error_destinatary_email, Toast.LENGTH_LONG).show();
                else {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{editText3.getText().toString()});
                    intent.putExtra(Intent.EXTRA_CC, new String[]{""});
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.reversi_result));
                    intent.putExtra(Intent.EXTRA_TEXT, editText2.getText().toString());
                    startActivity(intent);
                }
                break;
        }
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

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
