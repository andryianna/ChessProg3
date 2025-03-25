package Pieces;

import GUI.ChessBoard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Pawn implements Piece {
    private final String color;
    private final BufferedImage image;
    private int rank;
    private char file;

    public Pawn(String color,String imagePath,int rank,char file) {
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
    @Override
    public String getColor() { return color; }

    private BufferedImage loadImage(String path) {
        try {
            System.out.println("Loading image: " + path);
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        return isValidMove(startRank, startCol, endRank, endCol, board,null); // Chiama il metodo già esistente
    }

    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board, Move lastMove) {
        int direction = color.equals("white") ? 1 : -1;
        int startRow = color.equals("white") ? 1 : 6;
        int enPassantRow = color.equals("white") ? 4 : 3;

        Piece destinationPiece = board.getPiece(endRank, endCol);

        // Movimento in avanti
        if (startCol == endCol) {
            if (endRank == startRank + direction && destinationPiece == null) {
                return true;
            }
            if (startRank == startRow && endRank == startRank + (2 * direction) &&
                    destinationPiece == null && board.getPiece(startRank + direction, endCol) == null) {
                return true;
            }
        }

        // Cattura normale
        if (Math.abs(endCol - startCol) == 1 && endRank == startRank + direction) {
            if (destinationPiece != null && !destinationPiece.getColor().equals(this.color)) {
                return true;
            }
        }

        // Cattura En Passant
        if (startRank == enPassantRow && Math.abs(endCol - startCol) == 1 && endRank == startRank + direction) {
            if (lastMove != null && lastMove.getPiece() instanceof Pawn &&
                    lastMove.getStartRank() == startRank + (2 * direction) &&
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
