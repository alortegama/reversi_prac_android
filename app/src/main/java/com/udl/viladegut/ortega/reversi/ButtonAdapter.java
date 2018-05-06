package com.udl.viladegut.ortega.reversi;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private Board board;
    private Game game;
    private Player player;
    Button button;

    private OnItemActionClickListener listener;

    public ButtonAdapter(Context context, Board board, Game game, Player player, OnItemActionClickListener listener) {
        mContext = context;
        this.board = board;
        this.game = game;
        this.player = player;
        this.listener=listener;
    }

    public int getCount() {
        Log.d("Size", "El tamaño del board es " + board.size());
        return board.size() * board.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new Button(mContext);
            switch (board.size()) {
                case 4:
                    button.setLayoutParams(new ViewGroup.LayoutParams(70, 70));
                    button.setPadding(5, 5, 5, 5);
                    break;
                case 6:
                    button.setLayoutParams(new ViewGroup.LayoutParams(45, 45));
                    button.setPadding(2, 2, 2, 2);
                    break;
                case 8:
                    button.setLayoutParams(new ViewGroup.LayoutParams(35, 35));
                    button.setPadding(2, 2, 2, 2);
                    break;
            }
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else
            button = (Button) convertView;

        button.setId(position);

        Log.d("Button created", "El boton se ha creado con el id " + button.getId());

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

}
