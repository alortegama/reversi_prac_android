package com.udl.viladegut.ortega.reversi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText2;
    TextView tvStateMatch;
    Boolean time;
    Log log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            time = bundle.getBoolean(Keys.KEY_TIME);
            log = bundle.getParcelable(Keys.KEY_LOG);

            EditText editText1 = findViewById(R.id.actualDateTime);
            editText1.setText(obtainDateTime());

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
    }

    private String obtainDateTime() {
        Date d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("d, MMMM 'del' yyyy", new Locale("es", "ES"));
        String data = fecc.format(d);
        SimpleDateFormat ho = new SimpleDateFormat("h:mm a", new Locale("es", "ES"));
        String hora = ho.format(d);

        return data + ", " + hora;
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
        switch (v.getId()) {
            case R.id.buttonGoBack:
                finish();
                break;
            case R.id.buttonExit:
                finishAffinity();
                break;
            case R.id.buttonSendEmail:
                EditText editText3 = findViewById(R.id.addressEmail);

                if (editText3.getText().toString().isEmpty())
                    Toast.makeText(v.getContext(), R.string.error_destinatary_email, Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{editText3.getText().toString()});
                    intent.putExtra(Intent.EXTRA_CC, new String[]{""});
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.reversi_result));
                    intent.putExtra(Intent.EXTRA_TEXT, editText2.getText().toString());
                    startActivity(intent);
                }

        }
    }
}
