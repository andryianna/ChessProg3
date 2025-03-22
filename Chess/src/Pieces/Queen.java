package Pieces;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Queen implements Piece {
    private final String color;
    private final BufferedImage image;

    public Queen(String color, String imagePath) {
        this.color = color;
        this.image = loadImage(imagePath);
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
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
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, Piece[][] board) {
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
            if (board[row][col] != null) {
                return false; /// Pezzo intermedio trovato
            }
            row += rowDirection;
            col += colDirection;
        }

        /// Controllo se la destinazione è vuota o contiene un pezzo avversario
        Piece destinationPiece = board[endRank][endCol];
        return destinationPiece == null || !destinationPiece.getColor().equals(this.color);
    }


    @Override
    public BufferedImage getImage() {
        return image;
    }
}
