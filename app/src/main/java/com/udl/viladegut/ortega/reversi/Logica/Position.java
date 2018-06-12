package com.udl.viladegut.ortega.reversi.Logica;

public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public Position move(Direction direction) {
        int newRow = this.row + direction.getChangeInRow();
        int newColumn = this.column + direction.getChangeInColumn();

        return new Position(newRow, newColumn);
    }
}
