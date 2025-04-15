package Pieces;

import GUI.ChessBoard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Rook implements Piece {
    private final String color;
    private int rank;
    private char file;
    private boolean hasMoved;

    public Rook(String color,int rank,char file) {
        this.color = color;
        this.rank = rank;
        this.file = file;
        this.hasMoved = false;
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

    public boolean hasMovedState(){
        return hasMoved;
    }

    public void setMoved(){
        this.hasMoved = true;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endCol, ChessBoard board) {
        /// Controllo se la mossa è in linea retta (stessa colonna o stessa riga)
        if (startRank != endRank && startFile != endCol) {
            return false;
        }

        int rowDirection = Integer.compare(endRank, startRank); /// 1 se va giù, -1 se va su, 0 se fermo
        int colDirection = Integer.compare(endCol, startFile);   /// 1 se va a destra, -1 se va a sinistra, 0 se fermo

        int row = startRank + rowDirection;
        char col = (char) (startFile + colDirection);

        /// Controllo che non ci siano pezzi intermedi
        while (row != endRank || col != endCol) {
            if (board.getPiece(row,col) != null) {
                return false; /// Pezzo intermedio trovato
            }
            row += rowDirection;
            col += (char) colDirection;
        }

        /// Controllo se la destinazione è vuota o contiene un pezzo avversario
        Piece destinationPiece = board.getPiece(endRank,endCol);
        return destinationPiece == null || !destinationPiece.getColor().equals(this.color);
    }
}
