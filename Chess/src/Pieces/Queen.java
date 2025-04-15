package Pieces;

import GUI.ChessBoard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Queen implements Piece {
    private final String color;
    private int rank;
    private char file;

    public Queen(String color,int rank,char file) {
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
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startCol);

        /// Controllo se la mossa è valida come Torre o Alfiere
        if (!((rankDiff == fileDiff) || (startRank == endRank || startCol == endCol))) {
            return false;
        }

        /// Determina la direzione del movimento
        int rowDirection = Integer.compare(endRank, startRank);  /// +1 verso il basso, -1 verso l'alto, 0 se fermo
        int colDirection = Integer.compare(endCol, startCol);    /// +1 verso destra, -1 verso sinistra, 0 se fermo

        int row = startRank + rowDirection;
        char col = (char) (startCol + colDirection);

        /// Controlla che il percorso sia libero
        while (row != endRank || col != endCol) {
            if (board.getPiece(row,col) != null) {
                return false; /// Pezzo intermedio trovato
            }
            row += rowDirection;
            col += colDirection;
        }

        /// Controllo se la destinazione è vuota o contiene un pezzo avversario
        Piece destinationPiece = board.getPiece(endRank,endCol);
        return destinationPiece == null || !destinationPiece.getColor().equals(this.color);
    }
}
