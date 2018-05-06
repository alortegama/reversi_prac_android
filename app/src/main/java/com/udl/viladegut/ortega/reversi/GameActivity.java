package com.udl.viladegut.ortega.reversi;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class GameActivity extends AppCompatActivity implements OnItemActionClickListener {

    private static final String PARCEL_PLAYER = "player";
    private static final String PARCEL_CPU = "cpu";
    private static final String PARCEL_GAME = "game";
    private static final String PARCEL_BOARD = "board";
    private static final String PARCEL_LOG = "log";

    /* Declaració d'elements del layout i nous objectes*/
    String namePlayer;
    int sizeTable;
    boolean time;
    Board board;
    Game game;
    Player player;
    Player cpu;
    int difficult;
    Log log;
    int totalTime = 0;

    /*Elements of view*/
    GridView gridView;
    TextView tvNamePlayer;
    TextView tvNumDiskPlayer;
    TextView tvNameCpu;
    TextView tvNumDiskCPU;
    TextView tvNumCellsRemaining;
    TextView tvSeconds;
    TextView tvNumberSeconds;
    CountDownTimer timer;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        itemsLayout();

        /* Obtenim els paràmetres de l'activitat anterior */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            time = bundle.getBoolean(Keys.KEY_TIME);
            difficult = bundle.getInt(Keys.KEY_DIFFICULT);
            log = bundle.getParcelable(Keys.KEY_LOG);

            namePlayer = log.getPlayer();
            sizeTable = log.getGrid();

            if (savedInstanceState != null) {
                board = savedInstanceState.getParcelable(PARCEL_BOARD);
                player = savedInstanceState.getParcelable(PARCEL_PLAYER);
                cpu = savedInstanceState.getParcelable(PARCEL_CPU);
                game = savedInstanceState.getParcelable(PARCEL_GAME);
                log = savedInstanceState.getParcelable(PARCEL_LOG);
                totalTime = Integer.parseInt(log.getTime_remaining()) * 1000;
            } else {
                board = new Board(sizeTable);
                player = new Player(Game.DISK_BLACK, true);
                cpu = new Player(Game.DISK_WHITE);
                game = new Game(board);
            }
            initGame();
        } else {
            Toast.makeText(this, R.string.error_init, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void itemsLayout() {
        gridView = findViewById(R.id.reversiTable);
        tvNamePlayer = findViewById(R.id.namePlayer1);
        tvNumDiskPlayer = findViewById(R.id.numDiskP1);
        tvNameCpu = findViewById(R.id.nameCpu1);
        tvNumDiskCPU = findViewById(R.id.numDiskC1);
        tvNumCellsRemaining = findViewById(R.id.numCellsRemaining);
        tvSeconds = findViewById(R.id.seconds);
        tvNumberSeconds = findViewById(R.id.numberSeconds);
    }

    private void initGame() {
        int color = (time) ?
                getResources().getColor(R.color.Red) :
                getResources().getColor(R.color.Blue);

        tvSeconds.setTextColor(color);
        tvNumberSeconds.setTextColor(color);

        /* Actualizar estadistiques del joc */
        tvNamePlayer.setText(namePlayer.concat(": "));
        tvNameCpu.setText(tvNameCpu.getText().toString().concat(": "));
        updateStadistics(board, tvNumDiskPlayer, tvNumDiskCPU, tvNumCellsRemaining);

        gridView.setNumColumns(sizeTable);
        ButtonAdapter adapter = new ButtonAdapter(this, board, game, getPlayerTurn(player, cpu), this);
        gridView.setAdapter(adapter);

        /* Si hi ha control de temps, es configura i s'activa el contador */
        if (time) {
            if (totalTime == 0)
                switch (board.size()) {
                    case 8:
                        totalTime = 60000;
                        break;
                    case 6:
                        totalTime = 45000;
                        break;
                    case 4:
                        totalTime = 25000;
                        break;
                }

            timer = new CountDownTimer(totalTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    log.setTime_remaining(String.valueOf(millisUntilFinished / 1000));
                    tvNumberSeconds.setText(String.valueOf(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    finishGame(1);
                }
            };
            timer.start();
        }
    }


    @SuppressLint("SetTextI18n")
    private void updateStadistics(Board board, TextView t2, TextView t3, TextView t4) {
        t2.setText(Integer.toString(board.getBlack()));
        t3.setText(Integer.toString(board.getWhite()));
        t4.setText(Integer.toString(board.getRemaining()));
    }

    private Player getPlayerTurn(Player p1, Player p2) {
        return (p1.isTurn()) ? p1 : p2;
    }

    private Player getOtherPlayerTurn(Player p1, Player p2) {
        return (p1.isTurn()) ? p2 : p1;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL_BOARD, board);
        outState.putParcelable(PARCEL_PLAYER, player);
        outState.putParcelable(PARCEL_CPU, cpu);
        outState.putParcelable(PARCEL_GAME, game);
        outState.putParcelable(PARCEL_LOG, log);
        if (time)
            timer.cancel();
    }

    @Override
    public void onClick(int position) {
        Position newPosition = new Position(position / board.size(), position % board.size());

        if (!game.canPlayPosition(player, newPosition)) {
            Toast.makeText(getApplicationContext(), R.string.grid_not_valid, Toast.LENGTH_SHORT).show();
            return;
        }

        /* Torn de l'usuari */
        game.move(newPosition, getPlayerTurn(player, cpu));

        /*Torn de la CPU*/
        if (game.canPlay(getOtherPlayerTurn(player, cpu)))
            playCPU();

        /* Actualitzar el tauler i les dades */
        gridView.setAdapter(new ButtonAdapter(getApplicationContext(), board, game, getPlayerTurn(player, cpu), this));
        updateStadistics(board, tvNumDiskPlayer, tvNumDiskCPU, tvNumCellsRemaining);

        /* Comprovar si la partida ha finalitzat */
        if (game.getFinished(player, cpu))
            finishGame(0);

    }

    private void playCPU() {
        player.changeTurn(cpu);
        /* Torn de la CPU */
        do
            switch (difficult) {
                case Keys.EASY:
                    game.move(game.easyMode(cpu), cpu);
                    break;
                case Keys.NORMAL:
                    game.move(game.normalMode(cpu), cpu);
                    break;
            }
        while (!game.canPlay(getOtherPlayerTurn(player, cpu)) && !game.getFinished(player, cpu));
        cpu.changeTurn(player);
    }

    private void finishGame(int state) {
        Bundle b = new Bundle();
        Intent intent = new Intent(GameActivity.this, ResultActivity.class);
        log.setNum_pieces_player(board.getBlack());
        log.setNum_pieces_cpu(board.getWhite());


        switch (state) {
            case 0: /* Finalització dins de temps */
                if (board.getBlack() > board.getWhite())
                    log.setResult(getString(R.string.win));
                else if (board.getBlack() < board.getWhite())
                    log.setResult(getString(R.string.lose));
                else if (board.getRemaining() != 0)
                    log.setResult(getString(R.string.block));
                else
                    log.setResult(getString(R.string.draw));

                if (time)
                    timer.cancel();
                break;

            case 1: /* Finalització per fora de temps */
                log.setResult(getString(R.string.timeout));
                break;
        }

        if (time)
            log.setTime_remaining(tvNumberSeconds.getText().toString());


        b.putBoolean(Keys.KEY_TIME,time);
        b.putParcelable(Keys.KEY_LOG,log);


        intent.putExtras(b);


        startActivity(intent);
        finish();

    }
}

