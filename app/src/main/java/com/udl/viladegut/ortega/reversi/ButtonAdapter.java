package com.udl.viladegut.ortega.reversi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.udl.viladegut.ortega.reversi.Logica.Board;
import com.udl.viladegut.ortega.reversi.Logica.Game;
import com.udl.viladegut.ortega.reversi.Logica.Player;
import com.udl.viladegut.ortega.reversi.Logica.Position;

public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private Board board;
    private Game game;
    private Player player;
    Button button;
    private int width;
    private int height;

    private OnItemActionClickListener listener;

    public ButtonAdapter(Context context, Board board, Game game, Player player, OnItemActionClickListener listener) {
        mContext = context;
        this.board = board;
        this.game = game;
        this.player = player;
        this.listener=listener;

        /* Obtener tamaño de la pantalla */
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    public int getCount() {
        return board.size() * board.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int widthImage;
        int heightImage;

        if(getRotation(mContext).equals("vertical")){ //es vertical o portrait.
            widthImage = (width)/(board.size() + board.size()/2);
            heightImage = (height/2) / (board.size() + board.size()/2);
        }else{ // es horizontal o landscape.
            widthImage = (width/2) / (board.size() + board.size()/2);
            heightImage = (height * 80 / 100) / (board.size() + board.size()/2);
        }



        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new Button(mContext);
            button.setLayoutParams(new ViewGroup.LayoutParams(widthImage, heightImage));
            button.setPadding(5, 5, 5, 5);
        } else
            button = (Button) convertView;

        button.setId(position);

        Position position1 = new Position(position / board.size(), position % board.size());
        if (board.isWhite(position1))
            button.setBackgroundResource(R.drawable.white);
        else if (board.isBlack(position1))
            button.setBackgroundResource(R.drawable.black);
        else if (game.canPlayPosition(player, position1) && player.getColorAssigned() == 0)
            button.setBackgroundResource(R.drawable.mark_black);
        else if (game.canPlayPosition(player, position1) && player.getColorAssigned() == 1)
            button.setBackgroundResource(R.drawable.mark_white);
        else
            button.setBackgroundResource(R.drawable.empty);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view.getId());
            }
        });

        return button;
    }

    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return "vertical";
            case Surface.ROTATION_90:
            default:
                return "horizontal";
        }
    }

}
