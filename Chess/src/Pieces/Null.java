package Pieces;

import GUI.ChessBoard;
import TurnObserver.TurnManager;

public class Null implements Piece{
    TurnManager turnObserver;

    public Null(TurnManager turnObserver) {
        this.turnObserver = turnObserver;
    }
    @Override
    public String color() {
        return turnObserver.getCurrentTurn().equals("black") ? "white" : "black";
    }

    @Override
    public boolean canAttack(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        return false;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        return false;
    }
}
