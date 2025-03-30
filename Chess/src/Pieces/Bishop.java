package Pieces;

import GUI.ChessBoard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Bishop implements Piece {
    private final String color;
    private final BufferedImage image;
    private int rank;
    private char file;

    public Bishop(String color,String imagePath,int rank,char file) {
        this.color = color;
        this.image = loadImage(imagePath);
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
    private BufferedImage loadImage(String path) {
        try {
            File image = new File(path);
            return ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getColor() {
        return color;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endCol, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startFile);

        /// L'alfiere si muove solo in diagonale
        if (rankDiff != fileDiff) {
            return false;
        }

        /// Determina la direzione del movimento
        int rowDirection = Integer.compare(endRank, startRank);  //// +1 verso il basso, -1 verso l'alto
        int colDirection = Integer.compare(endCol, startFile);    /// +1 verso destra, -1 verso sinistra

        int row = startRank + rowDirection;
        char col = (char) (startFile + colDirection);

        /// Controlla che il percorso sia libero
        while (row != endRank && col != endCol) {
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

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
