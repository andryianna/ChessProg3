package Pieces;

import GUI.ChessBoard;

public interface Piece {
    String color();
    boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board);}
