package com.udl.viladegut.ortega.reversi.Logica;


import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable{

    private boolean white;
    private boolean black;
    private boolean empty;

    public Cell (boolean white, boolean black, boolean empty){
        this.white = white;
        this.black = black;
        this.empty = empty;
    }

    protected Cell(Parcel in) {
        white = in.readByte() != 0;
        black = in.readByte() != 0;
        empty = in.readByte() != 0;
    }

    public static final Creator<Cell> CREATOR = new Creator<Cell>() {
        @Override
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };

    public boolean isEmpty(){
        return this.empty;
    }

    public boolean isBlack() {
        return black;
    }

    public boolean isWhite() {
        return white;
    }

    public Cell setBlack() {
        this.empty = false;
        this.white = false;
        this.black = true;

        return this;
    }

    public Cell setWhite() {
        this.empty = false;
        this.white = true;
        this.black = false;

        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (white ? 1 : 0));
        dest.writeByte((byte) (black ? 1 : 0));
        dest.writeByte((byte) (empty ? 1 : 0));
    }
}
