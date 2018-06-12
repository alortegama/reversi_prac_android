package com.udl.viladegut.ortega.reversi.Logica;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{
    private int colorAssigned;
    private boolean turn;

    public Player (int colorAssigned, boolean turn) {
        this.colorAssigned = colorAssigned;
        this.turn = turn;
    }

    public Player (int colorAssigned) {
        this.colorAssigned = colorAssigned;
        this.turn = false;
    }


    public Player(Parcel in) {
        colorAssigned = in.readInt();
        turn = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(colorAssigned);
        dest.writeByte((byte) (turn ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public int getColorAssigned() {
        return colorAssigned;
    }

    public boolean isTurn () {
        return turn;
    }

    public void changeTurn (Player otherPlayer) {
        if (this.turn) {
            this.turn = false;
            otherPlayer.turn = true;
        } else if (otherPlayer.turn){
            otherPlayer.turn = false;
            this.turn = true;
        }
    }

}
