package com.udl.viladegut.ortega.reversi.Logica;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

    public static final int DISK_BLACK = 0;
    public static final int DISK_WHITE = 1;

    private Board board;

    public Game(Board board) {
        this.board = board;
    }

    protected Game(Parcel in) {
        board = in.readParcelable(Board.class.getClassLoader());
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    /*******************************************************
     *           METODES BÀSICS DE LA CLASSE GAME
     *******************************************************/

    public boolean getFinished(Player p1, Player p2) {
        return (!canPlay(p1) && !canPlay(p2));
    }

    /***
     * Comprova si la posició passada per paràmetre correspont a una posició on hi ha una fitxa
     * del mateix color de la del jugador
     * @param player Jugador en que es vol fer la comparativa de les fitxes
     * @param position Posició on es troba la fitxa igual o no a la del jugador
     * @return Boolean. Si les fitxes són del mateix color o no
     */
    public boolean isSame(Player player, Position position) {
        if (player.getColorAssigned() == DISK_BLACK)
            return board.isBlack(position);
        else
            return (player.getColorAssigned() == DISK_WHITE) && board.isWhite(position);
    }

    public boolean isOther(Player player, Position position) {
        if (player.getColorAssigned() == DISK_BLACK)
            return this.board.isWhite(position);
        else
            return (player.getColorAssigned() == DISK_WHITE) && this.board.isBlack(position);
    }

    public boolean someSame(Player player, Position position, Direction direction) {
        if (!board.contains(position) || board.isEmpty(position))
            return false;
        else if (isSame(player, position))
            return true;
        return someSame(player, position.move(direction), direction);
    }

    /*******************************************************
     *         COMPROVACIÓ DE MOVIMENTS POSSIBLES
     *******************************************************/

    public boolean isReverseDirection(Player player, Position position, Direction direction) {
        /* Hi ha una fitxa però no se sap a quina distància*/
        return (someSame(player, position, direction) && isOther(player, position));
    }

    private boolean[] directionsOfReverse(Player player, Position position) {
        boolean[] bools = new boolean[Direction.ALL.length];

        for (int i = 0; i < Direction.ALL.length; i++) {
            Direction direction = Direction.ALL[i];
            bools[i] = (isReverseDirection(player, position.move(direction), direction));
        }
        return bools;
    }

    private static boolean allFalse(boolean[] bools) {
        for (int i = 0; i < Direction.ALL.length; i++)
            if (bools[i])
                return false;
        return true;
    }

    public boolean canPlayPosition(Player player, Position position) {
        return (board.isEmpty(position) && !allFalse(directionsOfReverse(player, position)));
    }

    public boolean canPlay(Player player) {
        for (int i = 0; i < board.size(); i++)
            for (int j = 0; j < board.size(); j++)
                if (canPlayPosition(player, new Position(i, j)))
                    return true;

        return false;
    }

    /*******************************************************
     *               EXECUCIÓ DE MOVIMENTS
     *******************************************************/

    private void disk(Position position, Player player) {
        if (board.isEmpty(position)) {
            if (player.getColorAssigned() == DISK_BLACK)
                board.setBlack(position);
            else if (player.getColorAssigned() == DISK_WHITE)
                board.setWhite(position);
        } else
            board.reverse(position);
    }

    private void reverse(Position position, Direction direction, Player player) {
        while (isOther(player, position)) {
            disk(position, player);
            position = position.move(direction);
        }
    }

    private void reverseAll(Position position, boolean[] directions, Player player) {
        for (int i = 0; i < directions.length; i++)
            if (directions[i])
                reverse(position.move(Direction.ALL[i]), Direction.ALL[i], player);
    }

    public void move(Position position, Player player) {
        if (!this.board.isEmpty(position))
            return;
        boolean[] directions = directionsOfReverse(player, position);
        if (allFalse(directions))
            return;
        reverseAll(position, directions, player);
        disk(position, player);
    }

    /* Difficulty */
    public Position easyMode(Player player) {
        int numSteps = 0;
        Position bestPosition= new Position(-1, -1);
        for (int i = 0; i < board.size(); i++)
            for (int j = 0; j < board.size(); j++)
                if (canPlayPosition(player, new Position(i, j)))
                    return new Position(i, j);
        return new Position(-1, -1);
    }
    /* Difficulty */
    public Position normalMode(Player player) {
        int numSteps = 0;
        Position bestPosition= new Position(-1, -1);
        for (int i = 0; i < board.size(); i++)
            for (int j = 0; j < board.size(); j++)
                for (int k = 0; k < Direction.ALL.length; k++) {
                    Direction direction = Direction.ALL[k];
                    if (canPlayPosition(player, new Position(i, j))) {
                        int steps = stepsToOutOfBoard(new Position(i, j), direction);
                        if (steps > numSteps) {
                            bestPosition = new Position(i, j);
                            numSteps = steps;
                        }
                    }
                }

        return bestPosition;
    }


    private int stepsToOutOfBoard(Position position, Direction direction) {
        int numSteps = 0;

        while (this.board.contains(position)) {
            numSteps++;
            position = position.move(direction);
        }
        return numSteps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(board, flags);
    }
}


