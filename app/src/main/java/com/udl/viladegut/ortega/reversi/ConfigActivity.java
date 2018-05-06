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

public class ConfigActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Button goBack = findViewById(R.id.buttonGoBack);
        Button startGame = findViewById(R.id.buttonStart);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, GameActivity.class);
                EditText playerName = findViewById(R.id.playerName);
                RadioGroup radioGroup = findViewById(R.id.radioGroup1);
                RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
                CheckBox checkBox = findViewById(R.id.checkbox1);
                Log log = new Log();

                /* Comprovacions caixa de text */
                if (playerName.getText().toString().isEmpty())
                    Toast.makeText(view.getContext(), R.string.error_name_player, Toast.LENGTH_LONG).show();
                else {
                    Bundle b = new Bundle();
                    /* Comprovacions del radio group */
                    int sizeGrid=0;
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioButton1:
                            sizeGrid=8;
                            break;
                        case R.id.radioButton2:
                            sizeGrid=6;
                            break;
                        case R.id.radioButton3:
                            sizeGrid=4;
                            break;
                    }
                    log.setGrid(sizeGrid);

                    /* Comprovacions del radio group */
                    switch (radioGroup2.getCheckedRadioButtonId()) {
                        case R.id.radioButton4:
                            b.putInt(Keys.KEY_DIFFICULT, Keys.EASY);
                            break;
                        case R.id.radioButton5:
                            b.putInt(Keys.KEY_DIFFICULT, Keys.NORMAL);
                            break;
                    }

                    /* Comprovacions del check box */
                    if (checkBox.isChecked())
                        b.putBoolean(Keys.KEY_TIME, true);
                    else
                        b.putBoolean(Keys.KEY_TIME, false);


                    log.setGrid(sizeGrid);
                    log.setPlayer(playerName.getText().toString());

                    b.putParcelable(Keys.KEY_LOG,log);
                    intent.putExtras(b);

                    startActivity(intent);
                }
            }
        });
    }
}
