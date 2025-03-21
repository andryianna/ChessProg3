package Pieces;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Rook implements Piece {
    private final String color;
    private final BufferedImage image;
    private boolean hasMoved;

    public Rook(String color,String imagePath){
        this.color = color;
        this.image = loadImage(imagePath);
        this.hasMoved = false;
    }

    public boolean hasMovedState(){
        return hasMoved;
    }

    public void setMoved(){
        this.hasMoved = true;
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
    public boolean isValidMove(int startRank, char startFile, int endRank, char endCol, Piece[][] board) {
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
            if (board[row][col] != null) {
                return false; /// Pezzo intermedio trovato
            }
            row += rowDirection;
            col += (char) colDirection;
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
