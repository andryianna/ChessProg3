package Pieces;

import java.awt.image.BufferedImage;

public interface Piece {
    String getColor();
    boolean isValidMove(int startRank, char startCol, int endRank, char endFile, Piece [][] board);
    BufferedImage getImage();
}
