package Pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pawn implements Piece {
    private final String color;
    private final BufferedImage image;

    public Pawn(String color,String imagePath){
        this.color = color;
        this.image = loadImage(imagePath);
    }
    @Override
    public String getColor() { return color; }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, Piece[][] board) {
        int rankDiff = endRank - startRank;
        int fileDiff = Math.abs(endFile - startFile);
        Piece targetPiece = board[endRank][endFile];

        if (color.equals("white")) {
            /** Movimento standard: avanti di 1 o 2 se in posizione iniziale*/
            if (fileDiff == 0 && targetPiece == null && (rankDiff == 1 || (startRank == 2 && rankDiff == 2))) {
                return true;
            }
            /** Cattura in diagonale*/
            if (fileDiff == 1 && rankDiff == 1 && isCapturablePiece(targetPiece)) {
                return true;
            }
        } else if (color.equals("black")) {
            /** Movimento standard: indietro di 1 o 2 se in posizione iniziale*/
            if (fileDiff == 0 && targetPiece == null && (rankDiff == -1 || (startRank == 7 && rankDiff == -2))) {
                return true;
            }
            /** Cattura in diagonale*/
            if (fileDiff == 1 && rankDiff == -1 && isCapturablePiece(targetPiece)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    /** Metodo per verificare se il pezzo può essere catturato (non deve essere un re)*/
    private boolean isCapturablePiece(Piece piece) {
        return piece != null && !piece.getClass().getSimpleName().equals("King") && !piece.getColor().equals(this.color);
    }
}
