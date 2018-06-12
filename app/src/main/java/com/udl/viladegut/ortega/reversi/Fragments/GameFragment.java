package com.udl.viladegut.ortega.reversi.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.udl.viladegut.ortega.reversi.ButtonAdapter;
import com.udl.viladegut.ortega.reversi.DataListener;
import com.udl.viladegut.ortega.reversi.Keys;
import com.udl.viladegut.ortega.reversi.Log;
import com.udl.viladegut.ortega.reversi.Logica.Board;
import com.udl.viladegut.ortega.reversi.Logica.Game;
import com.udl.viladegut.ortega.reversi.Logica.Player;
import com.udl.viladegut.ortega.reversi.Logica.Position;
import com.udl.viladegut.ortega.reversi.OnItemActionClickListener;
import com.udl.viladegut.ortega.reversi.R;
import com.udl.viladegut.ortega.reversi.ResultActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GameFragment extends Fragment implements OnItemActionClickListener, DataListener {

    private static final String PARCEL_PLAYER = "player";
    private static final String PARCEL_CPU = "cpu";
    private static final String PARCEL_GAME = "game";
    private static final String PARCEL_BOARD = "board";
    private static final String PARCEL_LOG = "log";
    private static final String TIMER = "timer";


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
    String inTirada;
    ButtonAdapter adapter;
    boolean firstTimer=true;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemsLayout();

        /* Obtenim els paràmetres de l'activitat anterior */
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {

            time = bundle.getBoolean(Keys.KEY_TIME);
            difficult = bundle.getInt(Keys.KEY_DIFFICULT);

            log = bundle.getParcelable(Keys.KEY_LOG);

            namePlayer = log.getPlayer();
            sizeTable = log.getGrid();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_init), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        if (savedInstanceState != null) {
            board = savedInstanceState.getParcelable(PARCEL_BOARD);
            player = savedInstanceState.getParcelable(PARCEL_PLAYER);
            cpu = savedInstanceState.getParcelable(PARCEL_CPU);
            game = savedInstanceState.getParcelable(PARCEL_GAME);
            log = savedInstanceState.getParcelable(PARCEL_LOG);
            firstTimer=savedInstanceState.getBoolean(TIMER);

        } else {
            board = new Board(sizeTable);
            player = new Player(Game.DISK_BLACK, true);
            cpu = new Player(Game.DISK_WHITE);
            game = new Game(board);
        }
        initGame();
    }

    private void itemsLayout() {
        gridView = this.getView().findViewById(R.id.reversiTable);
        tvNamePlayer = this.getView().findViewById(R.id.namePlayer1);
        tvNumDiskPlayer = this.getView().findViewById(R.id.numDiskP1);
        tvNameCpu = this.getView().findViewById(R.id.nameCpu1);
        tvNumDiskCPU = this.getView().findViewById(R.id.numDiskC1);
        tvNumCellsRemaining = this.getView().findViewById(R.id.numCellsRemaining);
        tvSeconds = this.getView().findViewById(R.id.seconds);
        tvNumberSeconds = this.getView().findViewById(R.id.numberSeconds);
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

        adapter = new ButtonAdapter(getActivity(), board, game, getPlayerTurn(player, cpu), this);
        gridView.setAdapter(adapter);

        sendData("Alias: " + log.getPlayer());
        sendData("Mida graella: " + log.getGrid());
        if (time)
            sendData("Control del temps");
        else
            sendData("No hi ha control del temps\n");

        /* Si hi ha control de temps, es configura i s'activa el contador */
        if (time) {
            if (firstTimer) {
                firstTimer = false;
                switch (board.size()) {
                    case 8:
                        log.setTime_remaining("60");
                        break;
                    case 6:
                        log.setTime_remaining("45");
                        break;
                    case 4:
                        log.setTime_remaining("25");
                        break;
                }
            }
        }
        inTirada = obtainTime();
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
        outState.putBoolean(TIMER,firstTimer);
        if (time) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    public void onClick(int position) {
        Position newPosition = new Position(position / board.size(), position % board.size());

        if (!game.canPlayPosition(player, newPosition)) {
            Toast.makeText(getActivity(), R.string.grid_not_valid, Toast.LENGTH_SHORT).show();
            return;
        }

        /* Torn de l'usuari */
        game.move(newPosition, getPlayerTurn(player, cpu));

        /*Torn de la CPU*/
        if (game.canPlay(getOtherPlayerTurn(player, cpu)))
            playCPU();

        /* Actualitzar el tauler i les dades */
        adapter.notifyDataSetChanged();
        updateStadistics(board, tvNumDiskPlayer, tvNumDiskCPU, tvNumCellsRemaining);

        /* Comprovar si la partida ha finalitzat */
        if (game.getFinished(player, cpu))
            finishGame(0);

        sendData("> Casella seleccionada: (" + position / board.size() + ", " + position % board.size() + ")");
        sendData("   Nº caselles pendents: " + board.getRemaining());
        sendData("   Inici tirada: " + inTirada);
        sendData("   Final tirada: " + obtainTime());
        if (time)
            sendData("   Temps restant: " + log.getTime_remaining());
        sendData("\n");
        inTirada = obtainTime();
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
        Intent intent = new Intent(this.getActivity(), ResultActivity.class);
        log.setNum_pieces_player(board.getBlack());
        log.setNum_pieces_cpu(board.getWhite());
        log.setDate_time(obtainDateTime());
        if (time) {
            timer.cancel();
            log.setTime_remaining(tvNumberSeconds.getText().toString());
        }
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
                break;

            case 1: /* Finalització per fora de temps */
                log.setResult(getString(R.string.timeout));
                break;
        }


        b.putBoolean(Keys.KEY_TIME, time);
        b.putParcelable(Keys.KEY_LOG, log);

        intent.putExtras(b);

        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void sendData(String text) {
        LogFragment logFragment = (LogFragment) getFragmentManager().findFragmentById(R.id.logFragment);
        if (logFragment != null && logFragment.isInLayout())
            logFragment.putLog(text);
    }

    private String obtainTime() {
        String[] time = Calendar.getInstance().getTime().toString().split(" ");
        return time[3];
    }

    private static String obtainDateTime() {
        Date d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES"));
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        String data = fecc.format(d);
        SimpleDateFormat ho = new SimpleDateFormat("HH:mm:ss", new Locale("es", "ES"));
        String hora = ho.format(d);

        return data + " " + hora;
    }

    /***
     *
     * Controlación de tiempo cuando se sale del juego y vas a la multi-tarea.
     * Controlación de tiempo cuando se gira la pantalla.
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if (time)
            if (timer == null) {
                totalTime = Integer.parseInt(log.getTime_remaining()) * 1000;
                timer = new CountDownTimer(totalTime, 1000) {
                    public void onTick(long millisUntilFinished) {
                        log.setTime_remaining(String.valueOf(millisUntilFinished / 1000));
                        tvNumberSeconds.setText(String.valueOf(millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        finishGame(1);
                    }
                }.start();
            }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(time && timer!=null) {
            timer.cancel();
            timer = null;
        }
    }
}

