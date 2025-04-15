package Pieces;

import GUI.ChessBoard;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Knight implements Piece {
    private final String color;
    private int rank;
    private char file;

    public Knight(String color,int rank,char file) {
        this.color = color;
        this.rank = rank;
        this.file = file;
    }

    public void setPosition(int rank, char file) {
        this.rank = rank;
        this.file = file;
    }

    public int getRank() {
        return rank;
    }
    public char getFile() {
        return file;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endFile - startFile);
        boolean result = (rankDiff == 2 && fileDiff == 1) || (rankDiff == 1 && fileDiff == 2);

        /// Controlla se il movimento è valido (L-shape)
        if (!result) {
            return false;
        }
        Piece destinationPiece = board.getPiece(endRank, endFile);

        /// Se la casella di destinazione è vuota o ha un pezzo avversario, la mossa è valida
        return (destinationPiece == null || !destinationPiece.getColor().equals(this.getColor()));
    }
}
