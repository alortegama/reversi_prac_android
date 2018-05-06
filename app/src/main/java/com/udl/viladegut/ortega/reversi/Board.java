package com.udl.viladegut.ortega.reversi;


import android.os.Parcel;
import android.os.Parcelable;

public class Board implements Parcelable{

    private final int size;
    private Cell[][] cells;
    private int black;
    private int white;

    public Board(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
        this.black = 0;
        this.white = 0;
        initBoard();
    }


    protected Board(Parcel in) {
        size = in.readInt();
        black = in.readInt();
        white = in.readInt();
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    private void initBoard() {
        int half = size / 2;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                /* Assignació inicial de fitxes blanques */
                if ((i == half - 1 && j == half - 1) || (i == half && j == half))
                    this.cells[i][j] = new Cell(true, false, false);

                /* Assignació inicial de fitxes negres */
                else if ((i == half - 1 && j == half) || (i == half && j == half - 1))
                    this.cells[i][j] = new Cell(false, true, false);

                /* Assignació inicial de fitxes buides */
                else
                    this.cells[i][j] = new Cell(false, false, true);
            }
        this.black = 2;
        this.white = 2;
    }

    public int getBlack (){
        return this.black;
    }

    public int getWhite (){
        return this.white;
    }

    public int getRemaining () {
        return size * size - white - black;
    }

    public int size() {
        return size;
    }

    public boolean contains(Position p) {
        return p.getColumn() >= 0 && p.getColumn() <= size - 1
                && p.getRow() >= 0 && p.getRow() <= size - 1;
    }

    public boolean isEmpty(Position p) {
        return contains(p) && this.cells[p.getRow()][p.getColumn()].isEmpty();
    }

    public boolean isWhite(Position p) {
        return contains(p) && this.cells[p.getRow()][p.getColumn()].isWhite();
    }

    public boolean isBlack(Position p) {
        return contains(p) && this.cells[p.getRow()][p.getColumn()].isBlack();
    }

    public void setWhite(Position p) {
        if (this.contains(p) && this.isEmpty(p)) {
            this.cells[p.getRow()][p.getColumn()].setWhite();
            this.white++;
        }
    }

    public void setBlack(Position p) {
        if (this.contains(p) && this.isEmpty(p)) {
            this.cells[p.getRow()][p.getColumn()].setBlack();
            this.black++;
        }
    }

    public void reverse(Position p) {
        if (this.contains(p) && !this.isEmpty(p)) {
            if (this.isBlack(p)) {
                this.cells[p.getRow()][p.getColumn()].setWhite();
                this.black--;
                this.white++;
            }

            else if (this.isWhite(p)) {
                this.cells[p.getRow()][p.getColumn()].setBlack();
                this.white--;
                this.black++;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size);
        dest.writeInt(black);
        dest.writeInt(white);
    }
}