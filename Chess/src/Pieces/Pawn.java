package Pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
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
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, Piece[][] board) {
        return isValidMove(startRank, startCol, endRank, endCol, board,null); // Chiama il metodo già esistente
    }

    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, Piece[][] board, Move lastMove) {
        int direction = color.equals("white") ? 1 : -1;
        int startRow = color.equals("white") ? 1 : 6;
        int enPassantRow = color.equals("white") ? 4 : 3;

        // Movimento in avanti
        if (startCol == endCol) {
            if (endRank == startRank + direction && board[endRank][endCol] == null) {
                return true;
            }
            if (startRank == startRow && endRank == startRank + (2 * direction) && board[endRank][endCol] == null && board[startRank + direction][endCol] == null) {
                return true;
            }
        }

        // Cattura normale
        if (Math.abs(endCol - startCol) == 1 && endRank == startRank + direction) {
            if (board[endRank][endCol] != null && !board[endRank][endCol].getColor().equals(this.color)) {
                return true;
            }
        }

        // Cattura En Passant
        if (startRank == enPassantRow && Math.abs(endCol - startCol) == 1 && endRank == startRank + direction) {
            if (lastMove != null && lastMove.getPiece() instanceof Pawn && lastMove.getStartRank() == startRank + (2 * direction) &&
                    lastMove.getEndRank() == startRank && lastMove.getEndCol() == endCol) {
                return true;
            }
        }

        return false;
    }


    @Override
    public BufferedImage getImage() {
        return image;
    }

    /// Metodo per verificare se il pezzo può essere catturato (non deve essere un re)
    private boolean isCapturablePiece(Piece piece) {
        return piece != null && !piece.getClass().getSimpleName().equals("King") && !piece.getColor().equals(this.color);
    }
}
