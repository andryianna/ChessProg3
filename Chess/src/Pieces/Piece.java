package Pieces;

import GUI.ChessBoard;

import java.awt.image.BufferedImage;

public interface Piece {
    String getColor();
    boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board);
    BufferedImage getImage();
}
