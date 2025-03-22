package Pieces;

public class Move {
    private final Piece piece;
    private final int startRank;
    private final char startCol;
    private final int endRank;
    private final char endCol;

    public Move(Piece piece, int startRank, char startCol, int endRank, char endCol) {
        this.piece = piece;
        this.startRank = startRank;
        this.startCol = startCol;
        this.endRank = endRank;
        this.endCol = endCol;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getStartRank() {
        return startRank;
    }

    public char getStartCol() {
        return startCol;
    }

    public int getEndRank() {
        return endRank;
    }

    public char getEndCol() {
        return endCol;
    }
}
